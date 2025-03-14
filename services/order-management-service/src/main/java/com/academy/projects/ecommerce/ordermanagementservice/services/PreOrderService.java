package com.academy.projects.ecommerce.ordermanagementservice.services;

import com.academy.projects.ecommerce.ordermanagementservice.clients.services.ICartToOrderService;
import com.academy.projects.ecommerce.ordermanagementservice.clients.services.UserManagementServiceClient;
import com.academy.projects.ecommerce.ordermanagementservice.models.DeliveryFeasibility;
import com.academy.projects.ecommerce.ordermanagementservice.dtos.UpdateOrderRequestDto;
import com.academy.projects.ecommerce.ordermanagementservice.exceptions.*;
import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.Action;
import com.academy.projects.ecommerce.ordermanagementservice.kafka.producers.services.IOrderUpdateManager;
import com.academy.projects.ecommerce.ordermanagementservice.models.*;
import com.academy.projects.ecommerce.ordermanagementservice.repositories.DeliveryFeasibilityRepository;
import com.academy.projects.ecommerce.ordermanagementservice.repositories.PreOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class PreOrderService implements IPreOrderService {

    private final PreOrderRepository preOrderRepository;
    private final ICartToOrderService cartToOrderService;
    private final UserManagementServiceClient userManagementServiceClient;
    private final IInventoryService inventoryService;
    private final IOrderUpdateManager orderUpdateManager;
    private final IOrderService orderService;
    private final IDetailsService detailsService;
    private final DeliveryFeasibilityRepository deliveryFeasibilityRepository;

    private final Logger logger = LoggerFactory.getLogger(PreOrderService.class);
    private final PaymentDetailsService paymentDetailsService;

    @Autowired
    public PreOrderService(PreOrderRepository preOrderRepository, ICartToOrderService cartToOrderService, UserManagementServiceClient userManagementServiceClient, IInventoryService inventoryService, IOrderUpdateManager orderUpdateManager, IOrderService orderService, IDetailsService detailsService, DeliveryFeasibilityRepository deliveryFeasibilityRepository, PaymentDetailsService paymentDetailsService) {
        this.preOrderRepository = preOrderRepository;
        this.cartToOrderService = cartToOrderService;
        this.userManagementServiceClient = userManagementServiceClient;
        this.inventoryService = inventoryService;
        this.orderUpdateManager = orderUpdateManager;
        this.orderService = orderService;
        this.detailsService = detailsService;
        this.deliveryFeasibilityRepository = deliveryFeasibilityRepository;
        this.paymentDetailsService = paymentDetailsService;
    }

    @Override
    public PreOrder checkout(String customerId, String addressId) {
        if(preOrderRepository.existsByCustomerIdAndOrderStatus(customerId, PreOrderStatus.PENDING_FOR_PAYMENT)) throw new PendingOrderExistsException(customerId);
        Set<OrderItem> orderItems = cartToOrderService.getOrderItems();

        // Validate and get ETA
        List<DeliveryFeasibility> feasibilityDetails = this.validateAndCalculateETA(orderItems, addressId);

        // Request Inventory Service to Block the Inventory
        inventoryService.block(orderItems);

        PreOrder preOrder = createPreOrder(customerId, orderItems, feasibilityDetails, addressId);

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

        PaymentDetails paymentDetails = preOrder.getPaymentDetails();
        if(paymentDetails != null)
            paymentDetails = paymentDetailsService.save(paymentDetails);

        preOrder.setPaymentDetails(paymentDetails);

        preOrder = preOrderRepository.save(preOrder);

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
                orderUpdateManager.sendUpdate(preOrder, Action.CANCEL_REQUESTED);
            } else if(preOrder.getOrderStatus().equals(PreOrderStatus.CONVERTED)) {
                preOrder = orderService.createOrders(preOrder);
                // Send Order created notification to the user
                sendOrderUpdate(preOrder);
            }
        }

        preOrder = preOrderRepository.save(preOrder);

        logger.info("Pre Order {} updated successfully!!!", preOrder);
        return preOrder;
    }

    private void sendOrderUpdate(PreOrder preOrder) {
        for(Order order : preOrder.getOrders()) {
            orderUpdateManager.sendUpdate(order, Action.CREATE);
        }
    }

    @Override
    public List<PreOrder> getOrders(String customerId, PreOrderStatus preOrderStatus, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        if(preOrderStatus != null) return preOrderRepository.findAllByCustomerIdAndOrderStatus(customerId, preOrderStatus, pageable);
        else return preOrderRepository.findAllByCustomerId(customerId);
    }

    @Override
    public PreOrder getOrNull(String orderId) {
        return preOrderRepository.findById(orderId).orElse(null);
    }

    private PreOrderStatus from(PaymentStatus paymentStatus) {
        return switch (paymentStatus) {
            case PAID -> PreOrderStatus.CONVERTED;
            case CANCELLED, FAILED -> PreOrderStatus.CANCELLED;
            default -> PreOrderStatus.PENDING_FOR_PAYMENT;
        };
    }

    private PreOrder createPreOrder(String customerId, Set<OrderItem> orderItems, List<DeliveryFeasibility> feasibilityDetails, String addressId) {
        PreOrder preOrder = new PreOrder();
        preOrder.setCustomerId(customerId);
        preOrder.setPreOrderItems(from(orderItems, feasibilityDetails));
        preOrder.setOrders(new LinkedList<>());
        preOrder.setOrderStatus(PreOrderStatus.PENDING_FOR_PAYMENT);
        preOrder.setTotalAmount(getTotalAmount(orderItems));
        preOrder.setShippingAddressId(addressId);
        preOrder.setCreatedAt(new Date());
        return preOrderRepository.save(preOrder);
    }

    private Set<PreOrderItem> from(Set<OrderItem> orderItems, List<DeliveryFeasibility> feasibilityDetails) {
        int i = 0;
        Set<PreOrderItem> preOrderItems = new LinkedHashSet<>();
        for(OrderItem orderItem : orderItems) {
            PreOrderItem preOrderItem = from(orderItem);
            DeliveryFeasibility deliveryFeasibility = deliveryFeasibilityRepository.save(feasibilityDetails.get(i));
            preOrderItem.setDeliveryFeasibilityDetails(deliveryFeasibility);
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

    private List<DeliveryFeasibility> validateAndCalculateETA(Set<OrderItem> orderItems, String addressId) {
        Address customerAddress = this.getCustomerAddress(addressId);
        return getFeasibilityDetails(orderItems, customerAddress);
    }

    private List<DeliveryFeasibility> getFeasibilityDetails(Set<OrderItem> orderItems, Address customerAddress) {
        List<DeliveryFeasibility> feasibilityDetails = new LinkedList<>();
        for(OrderItem orderItem : orderItems) {
            DeliveryFeasibility feasibilityDetail = detailsService.checkFeasibilityAndETA(orderItem, customerAddress);
            if(feasibilityDetail.getIsFeasible()) feasibilityDetails.add(feasibilityDetail);
            else throw new DeliveryNotPossibleException(feasibilityDetail.getReason());
        }
        return feasibilityDetails;
    }

    private Address getCustomerAddress(String addressId) {
        try {
            ResponseEntity<Address> customerAddress = userManagementServiceClient.getCustomerAddress(addressId);
            return customerAddress.getBody();
        } catch (Exception e) {
            throw new AddressNotFoundException(addressId, e.getMessage());
        }
    }
}
