package com.academy.projects.ecommerce.ordermanagementservice.services;

import com.academy.projects.ecommerce.ordermanagementservice.clients.services.ICartToOrderService;
import com.academy.projects.ecommerce.ordermanagementservice.clients.services.ProductSearchServiceClient;
import com.academy.projects.ecommerce.ordermanagementservice.clients.services.UserManagementServiceClient;
import com.academy.projects.ecommerce.ordermanagementservice.dtos.DeliveryFeasibilityDetails;
import com.academy.projects.ecommerce.ordermanagementservice.dtos.DeliveryFeasibilityItem;
import com.academy.projects.ecommerce.ordermanagementservice.dtos.DeliveryFeasibilityRequestDto;
import com.academy.projects.ecommerce.ordermanagementservice.dtos.UpdateOrderRequestDto;
import com.academy.projects.ecommerce.ordermanagementservice.exceptions.*;
import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.Action;
import com.academy.projects.ecommerce.ordermanagementservice.kafka.producers.services.IOrderUpdateManager;
import com.academy.projects.ecommerce.ordermanagementservice.models.*;
import com.academy.projects.ecommerce.ordermanagementservice.repositories.PreOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class PreOrderService implements IPreOrderService {

    private final PreOrderRepository preOrderRepository;
    private final ICartToOrderService cartToOrderService;
    private final UserManagementServiceClient userManagementServiceClient;
    private final ProductSearchServiceClient productSearchServiceClient;
    private final IInventoryService inventoryService;
    private final IOrderUpdateManager orderUpdateManager;
    private final IOrderService orderService;

    private final Logger logger = LoggerFactory.getLogger(PreOrderService.class);

    @Autowired
    public PreOrderService(PreOrderRepository preOrderRepository, ICartToOrderService cartToOrderService, UserManagementServiceClient userManagementServiceClient, ProductSearchServiceClient productSearchServiceClient, IInventoryService inventoryService, IOrderUpdateManager orderUpdateManager, IOrderService orderService) {
        this.preOrderRepository = preOrderRepository;
        this.cartToOrderService = cartToOrderService;
        this.userManagementServiceClient = userManagementServiceClient;
        this.productSearchServiceClient = productSearchServiceClient;
        this.inventoryService = inventoryService;
        this.orderUpdateManager = orderUpdateManager;
        this.orderService = orderService;
    }

    @Override
    public PreOrder checkout(String customerId, String addressId) {
        if(preOrderRepository.existsByCustomerIdAndOrderStatus(customerId, PreOrderStatus.PENDING_FOR_PAYMENT)) throw new PendingOrderExistsException(customerId);
        Set<OrderItem> orderItems = cartToOrderService.getOrderItems();

        // Validate and get ETA
        List<DeliveryFeasibilityDetails> feasibilityDetails = this.validateAndCalculateETA(orderItems, addressId);

        // Request Inventory Service to Block the Inventory
        inventoryService.block(orderItems);

        PreOrder preOrder = createPreOrder(customerId, orderItems, feasibilityDetails);

        // Sending update to Kafka
        orderUpdateManager.sendUpdate(preOrder, Action.CREATE);

        logger.info("Pre Order {} created successfully!!!", preOrder);
        return preOrder;
    }

    @Override
    public PreOrder update(UpdateOrderRequestDto requestDto) {
        PreOrder preOrder = preOrderRepository.findById(requestDto.getOrderId()).orElseThrow(() -> new PreOrderNotFoundException(requestDto.getOrderId()));
        PaymentStatus paymentStatus = PaymentStatus.IN_PROCESSING;
        if(requestDto.getPaymentDetails() != null) {
            preOrder.setPaymentDetails(requestDto.getPaymentDetails());
            if(requestDto.getPaymentDetails().getPaymentStatus() != null)
                paymentStatus = requestDto.getPaymentDetails().getPaymentStatus();
        }

        if(preOrder.getOrderStatus().equals(PreOrderStatus.CANCELLED)) {
            if(paymentStatus.equals(PaymentStatus.PAID)) {
                // Sent Payment Service to Refund
                orderUpdateManager.sendUpdate(preOrder, Action.REFUND);
            }
        } else {
            preOrder.setOrderStatus(from(paymentStatus));
            if(preOrder.getOrderStatus().equals(PreOrderStatus.CANCELLED)) {
                // Request Inventory Service to Release the Inventory
                inventoryService.releaseInventory(preOrder.getPreOrderItems());

                // Sending Update to Kafka
                orderUpdateManager.sendUpdate(preOrder, Action.CANCELLED);
            } else if(preOrder.getOrderStatus().equals(PreOrderStatus.CONVERTED)) {
                preOrder = orderService.createOrders(preOrder);
                // Send Order created notification to the user
                orderUpdateManager.sendUpdate(preOrder, Action.CREATE);
            }
        }

        preOrder = preOrderRepository.save(preOrder);
        logger.info("Pre Order {} updated successfully!!!", preOrder);
        return preOrder;
    }

    @Override
    public List<PreOrder> getOrders(String customerId, PreOrderStatus preOrderStatus, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        if(preOrderStatus != null) return preOrderRepository.findAllByCustomerIdAndOrderStatus(customerId, preOrderStatus, pageable);
        else return preOrderRepository.findAllByCustomerId(customerId);
    }

    private PreOrderStatus from(PaymentStatus paymentStatus) {
        return switch (paymentStatus) {
            case PAID -> PreOrderStatus.CONVERTED;
            case CANCELLED, FAILED -> PreOrderStatus.CANCELLED;
            default -> PreOrderStatus.PENDING_FOR_PAYMENT;
        };
    }

    private PreOrder createPreOrder(String customerId, Set<OrderItem> orderItems, List<DeliveryFeasibilityDetails> feasibilityDetails) {
        PreOrder preOrder = new PreOrder();
        preOrder.setCustomerId(customerId);
        preOrder.setPreOrderItems(from(orderItems, feasibilityDetails));
        preOrder.setOrders(new LinkedList<>());
        preOrder.setOrderStatus(PreOrderStatus.PENDING_FOR_PAYMENT);
        preOrder.setTotalAmount(getTotalAmount(orderItems));
        return preOrderRepository.save(preOrder);
    }

    private Set<PreOrderItem> from(Set<OrderItem> orderItems, List<DeliveryFeasibilityDetails> feasibilityDetails) {
        int i = 0;
        Set<PreOrderItem> preOrderItems = new LinkedHashSet<>();
        for(OrderItem orderItem : orderItems) {
            PreOrderItem preOrderItem = from(orderItem);
            preOrderItem.setDeliveryFeasibilityDetails(feasibilityDetails.get(i));
            preOrderItems.add(preOrderItem);
            i++;
        }
        return preOrderItems;
    }

    private BigDecimal getTotalAmount(Set<OrderItem> orderItems) {
        return orderItems.stream().map(OrderItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private PreOrderItem from(OrderItem orderItem) {
        PreOrderItem preOrderItem = new PreOrderItem();
        preOrderItem.setProductId(orderItem.getProductId());
        preOrderItem.setProductName(orderItem.getProductName());
        preOrderItem.setVariantId(orderItem.getVariantId());
        preOrderItem.setSellerId(orderItem.getSellerId());
        preOrderItem.setQuantity(orderItem.getQuantity());
        preOrderItem.setUnitPrice(orderItem.getUnitPrice());
        return preOrderItem;
    }

    private List<DeliveryFeasibilityDetails> validateAndCalculateETA(Set<OrderItem> orderItems, String addressId) {
        Address customerAddress = this.getCustomerAddress(addressId);
        List<DeliveryFeasibilityDetails> feasibilityDetails = getDeliveryFeasibilityDetails(orderItems, customerAddress);
        feasibilityDetails.forEach(feasibilityDetail -> {
            if(!feasibilityDetail.getIsFeasible()) throw new DeliveryNotPossibleException(feasibilityDetail.getReason());
        });
        return feasibilityDetails;
    }

    private DeliveryFeasibilityRequestDto from(Set<OrderItem> orderItems, Address customerAddress) {
        List<DeliveryFeasibilityItem> deliveryFeasibilityItems = new LinkedList<>();
        for(OrderItem orderItem : orderItems) {
            deliveryFeasibilityItems.add(DeliveryFeasibilityItem.builder()
                    .sellerId(orderItem.getSellerId())
                    .variantId(orderItem.getVariantId())
                    .quantity(orderItem.getQuantity())
                    .unitPrice(orderItem.getUnitPrice())
                    .build());
        }
        return DeliveryFeasibilityRequestDto.builder()
                .customerAddress(customerAddress)
                .items(deliveryFeasibilityItems)
                .build();
    }

    private Address getCustomerAddress(String addressId) {
        try {
            ResponseEntity<Address> customerAddress = userManagementServiceClient.getCustomerAddress(addressId);
            return customerAddress.getBody();
        } catch (Exception e) {
            throw new AddressNotFoundException(addressId, e.getMessage());
        }
    }

    private List<DeliveryFeasibilityDetails> getDeliveryFeasibilityDetails(Set<OrderItem> orderItems, Address customerAddress) {
        try {
            ResponseEntity<List<DeliveryFeasibilityDetails>> details = productSearchServiceClient.getDeliveryFeasibilityDetails(from(orderItems, customerAddress));
            return details.getBody();
        } catch(Exception e) {
            throw new RuntimeException("Failed to get the delivery feasibility details!!! >>> " + e.getMessage());
        }
    }
}
