package com.academy.projects.ecommerce.productsearchservice.services;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.indices.AnalyzeRequest;
import co.elastic.clients.elasticsearch.indices.AnalyzeResponse;
import co.elastic.clients.elasticsearch.indices.analyze.AnalyzeToken;
import com.academy.projects.ecommerce.productsearchservice.dtos.DeliveryDetails;
import com.academy.projects.ecommerce.productsearchservice.dtos.ProductFilters;
import com.academy.projects.ecommerce.productsearchservice.dtos.ProductSearchDto;
import com.academy.projects.ecommerce.productsearchservice.exceptions.AddressNotProvidedException;
import com.academy.projects.ecommerce.productsearchservice.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ProductSearchService implements IProductSearchService {
    private final ElasticsearchClient elasticsearchClient;
    private final ElasticsearchOperations elasticsearchOperations;
    private final IInventoryService inventoryService;
    private final ISellerService sellerService;

    private final Logger logger = LoggerFactory.getLogger(ProductSearchService.class);

    @Autowired
    public ProductSearchService(ElasticsearchClient elasticsearchClient, ElasticsearchOperations elasticsearchOperations, IInventoryService inventoryService, ISellerService sellerService) {
        this.elasticsearchClient = elasticsearchClient;
        this.elasticsearchOperations = elasticsearchOperations;
        this.inventoryService = inventoryService;
        this.sellerService = sellerService;
    }
    @Override
    public List<Product> search(ProductSearchDto searchDto, int page, int pageSize) {
        try {
            validateUserAddress(searchDto.getUserAddress());
            Pageable pageable = PageRequest.of(page, pageSize);

            List<String> words = analyze(searchDto.getQuery());
            String updatedQuery = getQuery(words);

            NativeQuery nativeQuery = getQuery(updatedQuery, searchDto.getFilters(), pageable);
            logger.info("Query: {}", Objects.requireNonNull(nativeQuery.getQuery()));
            SearchHits<Product> searchHits = elasticsearchOperations.search(nativeQuery, Product.class);
            List<Product> foundProducts = searchHits.stream().map(SearchHit::getContent).toList();
            foundProducts = filterProducts(foundProducts, searchDto.getUserAddress());
            return foundProducts;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return List.of();
        }
    }

    private void validateUserAddress(Address address) {
        if(address == null) throw new AddressNotProvidedException("User Address not provided for the search!!!");
        if(address.getCountry() == null) throw new AddressNotProvidedException("Country not provided for the search!!!");
        if(address.getZip() == null) throw new AddressNotProvidedException("Zip code not provided for the search!!!");
    }

    private List<Product> filterProducts(List<Product> foundProducts, Address userAddress) {
        List<Product> filteredProducts = new ArrayList<>();
        for (Product product : foundProducts) {
            boolean hasVariant = false;
            for(Variant variant : product.getVariants()) {
                List<InventoryUnit> inventoryUnits = inventoryService.getAll(product.getProductId(), variant.getVariantId());
                DeliveryDetails deliveryDetails = getDetails(inventoryUnits, userAddress);
                if(deliveryDetails.getInStock()) {
                    hasVariant = true;
                    variant.setDeliveryDetails(deliveryDetails);
                }
            }
            if(hasVariant) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }

    private DeliveryDetails getDetails(List<InventoryUnit> inventoryUnits, Address userAddress) {
        DeliveryDetails deliveryDetails = new DeliveryDetails();
        long etaInHours = Long.MAX_VALUE;
        Seller foundSeller = null;
        InventoryUnit foundInventoryUnit = null;
        for(InventoryUnit inventoryUnit : inventoryUnits) {
            if(inventoryUnit.getQuantity() <= 0) continue;
            Seller seller = sellerService.getBySellerId(inventoryUnit.getSellerId());
            if(!seller.getAddress().getCountry().equalsIgnoreCase(userAddress.getCountry())) continue;
            long calculatedEta = calculateEta(userAddress.getZip(), seller.getAddress().getZip());
            if((calculatedEta > 0) && (calculatedEta < etaInHours)) {
                etaInHours = calculatedEta;
                foundSeller = seller;
                foundInventoryUnit = inventoryUnit;
            }
        }
        if(foundSeller != null) {
            deliveryDetails.setInStock(true);
            deliveryDetails.setEta(from(etaInHours));
            deliveryDetails.setPrice(foundInventoryUnit.getUnitPrice());
            deliveryDetails.setSellerId(foundSeller.getSellerId());
        }
        return deliveryDetails;
    }

    private static Date from(long etaInHours) {
        Date current = new Date();
        current.setTime(current.getTime() + (etaInHours * 60 * 60 * 1000));
        return current;
    }

    private long calculateEta(String userZipCode, String sellerZipCode) {
        try {
            int[] hours = { 3, 6, 12, 18, 24, 48 };
            long userZip = Long.parseLong(userZipCode);
            long sellerZip = Long.parseLong(sellerZipCode);
            long difference = Math.abs(userZip - sellerZip);
            int index = 0;
            long calculatedEta = 0;
            while(difference > 0) {
                long digit = difference % 10;
                calculatedEta += (hours[index] * digit);
                difference = difference / 10;
                index++;
            }
            if(calculatedEta < 24)
                calculatedEta = 24;
            return calculatedEta;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0;
        }
    }

    private NativeQuery getQuery(String query, ProductFilters filters, Pageable pageable) {
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();

        List<Query> queries = new LinkedList<>();
        queries.add(this.getQuery(query));

        if(filters != null) {
            queries.addAll(this.getQueries(filters));
            queries.addAll(this.getQueries(filters.getAttributes(), "attributes."));
            queries.addAll(this.getQueries(filters.getVariantAttributes(), "variants.variantAttributes."));
        }

        boolQuery.must(queries);
        NativeQueryBuilder nativeQueryBuilder = new NativeQueryBuilder().withQuery(boolQuery.build()._toQuery()).withPageable(pageable);
        return nativeQueryBuilder.build();
    }

    private Query getQuery(String fieldName, List<String> values) {
        return new Query.Builder().match(new MatchQuery.Builder().field(fieldName).query(String.join(" ", values)).operator(Operator.Or).fuzziness("1").prefixLength(2).build()).build();
    }

    private Query getQuery(String baseQuery) {
        return new Query.Builder().queryString(new QueryStringQuery.Builder().query(baseQuery).build()).build();
    }

    private List<Query> getQueries(Map<String, List<String>> variantAttributes, String prefix) {
        List<Query> queries = new LinkedList<>();
        if((variantAttributes != null) && (!variantAttributes.isEmpty())) {
            for (Map.Entry<String, List<String>> entry : variantAttributes.entrySet()) {
                if ((entry.getValue() != null) && (!entry.getValue().isEmpty())) {
                    queries.add(this.getQuery(prefix + entry.getKey(), entry.getValue()));
                }
            }
        }
        return queries;
    }

    @SuppressWarnings("unchecked")
    private List<Query> getQueries(ProductFilters filters) {
        List<Query> queries = new LinkedList<>();
        Set<String> ignoreFields = Set.of("attributes", "variantAttributes", "searchAttributes");
        try {
            Class<ProductFilters> filtersClass = ProductFilters.class;
            Field[] categoryFields = filtersClass.getDeclaredFields();
            for (Field categoryField : categoryFields) {
                if(ignoreFields.contains(categoryField.getName())) continue;
                categoryField.setAccessible(true);
                Object value = categoryField.get(filters);
                if (value != null) {
                    List<String> values = (List<String>) value;
                    Query query = new Query.Builder().match(new MatchQuery.Builder().field(categoryField.getName()).query(String.join(" ", values)).fuzziness("2").prefixLength(2).build()).build();
                    queries.add(query);
                }
                categoryField.setAccessible(false);
            }
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
        }
        return queries;
    }

    private String getQuery(List<String> words) {
        return words.stream().map((word) -> (word.trim() + "~2")).collect(Collectors.joining(" AND "));
    }

    private List<String> analyze(String query) {
        try {
            AnalyzeRequest analyzeRequest = new AnalyzeRequest.Builder().analyzer("stop").text(query).build();
            AnalyzeResponse response = elasticsearchClient.indices().analyze(analyzeRequest);
            List<String> tokens = response.tokens().stream().map(AnalyzeToken::token).toList();
            ArrayList<String> numberedTokens = this.numberedWords(query);
            numberedTokens.addAll(tokens);
            return numberedTokens;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return List.of();
        }
    }

    private ArrayList<String> numberedWords(String query) {
        ArrayList<String> words = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\b\\w*\\d\\w*\\b");

        Matcher matcher = pattern.matcher(query);
        while (matcher.find()) {
            words.add(matcher.group());
        }
        return words;
    }
}
