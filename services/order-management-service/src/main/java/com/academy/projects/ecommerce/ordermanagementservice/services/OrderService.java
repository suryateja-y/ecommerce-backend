package com.academy.projects.ecommerce.ordermanagementservice.services;

import com.academy.projects.ecommerce.ordermanagementservice.exceptions.*;
import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.*;
import com.academy.projects.ecommerce.ordermanagementservice.kafka.producers.services.IOrderUpdateManager;
import com.academy.projects.ecommerce.ordermanagementservice.models.*;
import com.academy.projects.ecommerce.ordermanagementservice.repositories.OrderItemRepository;
import com.academy.projects.ecommerce.ordermanagementservice.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final IOrderUpdateManager orderUpdateManager;
    private final IInvoiceService invoiceService;
    private final IInventoryService inventoryService;
    private final OrderItemRepository orderItemRepository;

    private final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final IPaymentDetailsService paymentDetailsService;
    private final TrackingDetailsService trackingDetailsService;

    @Autowired
    public OrderService(OrderRepository orderRepository, IOrderUpdateManager orderUpdateManager, InvoiceService invoiceService, IInventoryService inventoryService, OrderItemRepository orderItemRepository, IPaymentDetailsService paymentDetailsService, TrackingDetailsService trackingDetailsService) {
        this.orderRepository = orderRepository;
        this.orderUpdateManager = orderUpdateManager;
        this.invoiceService = invoiceService;
        this.inventoryService = inventoryService;
        this.orderItemRepository = orderItemRepository;
        this.paymentDetailsService = paymentDetailsService;
        this.trackingDetailsService = trackingDetailsService;
    }

    @Override
    public List<Order> getOrders(String customerId, OrderStatus orderStatus, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        if(orderStatus != null) return orderRepository.findAllByCustomerIdAndOrderStatus(customerId, orderStatus, pageable);
        else return orderRepository.findAllByCustomerId(customerId);
    }

    @Override
    public Order updateStatus(String customerId, String orderId, OrderStatus orderStatus) {
        Order order = orderRepository.findByIdAndCustomerId(orderId, customerId).orElseThrow(() -> new OrderNotFoundException(orderId, customerId));
        if(orderStatus.equals(OrderStatus.CANCELLED)) {
            if(order.getOrderStatus().equals(OrderStatus.CANCELLED)) return order;
            if(order.getOrderStatus().equals(OrderStatus.CREATED)) {
                inventoryService.release(order.getOrderItems());
                order.setOrderStatus(OrderStatus.CANCELLED);
                order = orderRepository.save(order);

                // Send details to Payment Service to refund
                orderUpdateManager.sendUpdate(order, Action.REFUND);
            } else throw new InvalidStatusRequest("Order is already completed!!! Can not cancel now!!!");
        }
        logger.info("Order '{}' status updated to '{}' successfully!!!", order.getId(), order.getOrderStatus());
        return order;
    }

    @Override
    public PreOrder createOrders(PreOrder preOrder) {
        PaymentDetails paymentDetails = preOrder.getPaymentDetails();
        Set<PreOrderItem> preOrderItems = preOrder.getPreOrderItems();
        Map<String, Set<PreOrderItem>> sellerPreOrders = get(preOrderItems);
        List<Order> orders = new LinkedList<>();

        for(Map.Entry<String, Set<PreOrderItem>> entry : sellerPreOrders.entrySet()) {
            Set<PreOrderItem> sellerPreOrderItems = entry.getValue();
            Order order = new Order();
            order.setCustomerId(preOrder.getCustomerId());
            order.setOrderStatus(OrderStatus.CREATED);
            order.setPreOrder(preOrder);
            order.setPaymentDetails(paymentDetailsForOrder(paymentDetails));
            Set<OrderItem> orderItems = from(sellerPreOrderItems);
            order.setOrderItems(orderItems);
            order.setTotalAmount(this.calculateTotalAmount(orderItems));
            order = orderRepository.save(order);
            Invoice invoice = invoiceService.createInvoice(order);
            order.setInvoice(invoice);
            order = orderRepository.save(order);
            orders.add(order);
        }
        preOrder.setOrders(orders);
        return preOrder;
    }

    private PaymentDetails paymentDetailsForOrder(PaymentDetails paymentDetails) {
        paymentDetails.setId(null);
        return  paymentDetailsService.save(paymentDetails);
    }
    private BigDecimal calculateTotalAmount(Set<OrderItem> orderItems) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for(OrderItem orderItem : orderItems) {
            totalAmount = totalAmount.add(orderItem.getTotalPrice());
        }
        return totalAmount;
    }
    @Override
    public Order updateTracking(TrackingDto trackingDto) {
        Order order = orderRepository.findById(trackingDto.getOrderId()).orElse(null);
        if(order != null) {
            if(trackingDto.getAction().equals(Action.CANCEL_REQUESTED)) {
                if(trackingDto.getActionStatus().equals(ActionStatus.SUCCEEDED)) {
                    order.getTrackingDetails().setTrackingStatus(TrackingStatus.CANCELLED);
                    order.getTrackingDetails().setComment("Order has been cancelled!!!");
                    markCancelled(order);
                } else {
                    order.getTrackingDetails().setTrackingStatus(trackingDto.getTrackingStatus());
                    order.getTrackingDetails().setComment(trackingDto.getMessage());
                }
            } else {
                TrackingDetails trackingDetails = new TrackingDetails();
                trackingDetails.setTrackingStatus(trackingDto.getTrackingStatus());
                trackingDetails.setComment(trackingDto.getMessage());
                trackingDetails.setTrackingNumber(trackingDto.getTrackingNumber());
                trackingDetails.setTrackingId(trackingDto.getTrackingId());
                trackingDetails = trackingDetailsService.save(trackingDetails);
                order.setTrackingDetails(trackingDetails);
            }

            if(order.getTrackingDetails().getTrackingStatus().equals(TrackingStatus.DELIVERED))
                order.setOrderStatus(OrderStatus.COMPLETED);

            order = orderRepository.save(order);
            logger.info("Order '{}' tracking updated successfully!!!", order.getId());
        }
        return order;
    }

    @Override
    public Order updatePayment(PaymentDto paymentDto) {
        Payment payment = paymentDto.getPayment();
        Order order = orderRepository.findById(payment.getOrderId()).orElse(null);
        if(order != null) {
            if(paymentDto.getAction().equals(Action.CANCEL_REQUESTED)) {
                if(paymentDto.getActionStatus().equals(ActionStatus.SUCCEEDED) && payment.getPaymentStatus().equals(PaymentStatus.REFUNDED)) {
                    order.getPaymentDetails().setPaymentStatus(PaymentStatus.CANCELLED);
                    order.getPaymentDetails().setComment("Order has been cancelled!!!");
                    markCancelled(order);
                } else {
                    order.getPaymentDetails().setPaymentStatus(payment.getPaymentStatus());
                    order.getPaymentDetails().setComment(paymentDto.getMessage());
                }
            } else {
                PaymentDetails paymentDetails = new PaymentDetails();
                paymentDetails.setPaymentStatus(payment.getPaymentStatus());
                paymentDetails.setComment(paymentDto.getMessage());
                paymentDetails.setPaymentDate(payment.getCreatedAt());
                paymentDetails.setPaymentId(payment.getPaymentId());
                paymentDetails.setCurrencyType(payment.getCurrency());
                paymentDetails.setPaymentMethod(payment.getPaymentMethod());
                order.setPaymentDetails(paymentDetails);
            }
            order = orderRepository.save(order);
            logger.info("Order '{}' payment details updated successfully!!!", order.getId());
        }
        return order;
    }

    private void markCancelled(Order order) {
        if(order.getOrderStatus().equals(OrderStatus.CANCEL_REQUESTED)) {
            boolean paymentCancelled = (order.getPaymentDetails().getPaymentStatus().equals(PaymentStatus.CANCELLED) || order.getPaymentDetails().getPaymentStatus().equals(PaymentStatus.REFUNDED));
            boolean trackingCancelled = order.getTrackingDetails().getTrackingStatus().equals(TrackingStatus.CANCELLED);
            if(paymentCancelled && trackingCancelled) order.setOrderStatus(OrderStatus.CANCELLED);
        }
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private Map<String, Set<PreOrderItem>> get(Set<PreOrderItem> preOrderItems) {
        Map<String, Set<PreOrderItem>> sellerPreOrders = new LinkedHashMap<>();

        for(PreOrderItem preOrderItem : preOrderItems) {
            Set<PreOrderItem> sellerPreOrderItems = sellerPreOrders.getOrDefault(preOrderItem.getSellerId(), new LinkedHashSet<>());
            sellerPreOrderItems.add(preOrderItem);
            sellerPreOrders.put(preOrderItem.getSellerId(), sellerPreOrderItems);
        }
        return sellerPreOrders;
    }

    private Set<OrderItem> from(Set<PreOrderItem> preOrderItems) {
        Set<OrderItem> orderItems = new LinkedHashSet<>();
        for(PreOrderItem preOrderItem : preOrderItems) {
            orderItems.add(from(preOrderItem));
        }
        return orderItems;
    }

    private OrderItem from(PreOrderItem preOrderItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setSellerId(preOrderItem.getSellerId());
        orderItem.setProductId(preOrderItem.getProductId());
        orderItem.setQuantity(preOrderItem.getQuantity());
        orderItem.setProductName(preOrderItem.getProductName());
        orderItem.setUnitPrice(preOrderItem.getUnitPrice());
        orderItem.setVariantId(preOrderItem.getVariantId());
        orderItem.setEta(preOrderItem.getDeliveryFeasibilityDetails().getEta());
        return orderItemRepository.save(orderItem);

    }

}
