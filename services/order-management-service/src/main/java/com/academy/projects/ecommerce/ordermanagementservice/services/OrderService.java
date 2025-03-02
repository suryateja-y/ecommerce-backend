package com.academy.projects.ecommerce.ordermanagementservice.services;

import com.academy.projects.ecommerce.ordermanagementservice.exceptions.*;
import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.Action;
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

import java.util.*;

@Service
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final IOrderUpdateManager orderUpdateManager;
    private final IInvoiceService invoiceService;
    private final IInventoryService inventoryService;
    private final OrderItemRepository orderItemRepository;

    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    public OrderService(OrderRepository orderRepository, IOrderUpdateManager orderUpdateManager, InvoiceService invoiceService, IInventoryService inventoryService, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderUpdateManager = orderUpdateManager;
        this.invoiceService = invoiceService;
        this.inventoryService = inventoryService;
        this.orderItemRepository = orderItemRepository;
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
    public Order updateStatus(String customerId, String orderId, PaymentStatus paymentStatus) {
        Order order = orderRepository.findByIdAndCustomerId(orderId, customerId).orElseThrow(() -> new OrderNotFoundException(orderId, customerId));
        if(paymentStatus.equals(PaymentStatus.REFUND)) {
            order.getPaymentDetails().setPaymentStatus(PaymentStatus.REFUND);
            order = orderRepository.save(order);
            logger.info("Order '{}' payment status updated to '{}' successfully!!!", order.getId(), PaymentStatus.REFUND);
        }
        return order;
    }

    @Override
    public PreOrder createOrders(PreOrder preOrder) {
        Set<PreOrderItem> preOrderItems = preOrder.getPreOrderItems();
        Map<String, Set<PreOrderItem>> sellerPreOrders = get(preOrderItems);
        List<Order> orders = new LinkedList<>();
        for(Map.Entry<String, Set<PreOrderItem>> entry : sellerPreOrders.entrySet()) {
            Set<PreOrderItem> sellerPreOrderItems = entry.getValue();
            Order order = new Order();
            order.setCustomerId(preOrder.getCustomerId());
            order.setOrderStatus(OrderStatus.CREATED);
            order.setPreOrder(preOrder);
            order.setPaymentDetails(preOrder.getPaymentDetails());
            Set<OrderItem> orderItems = from(sellerPreOrderItems);
            order.setOrderItems(orderItems);
            order = orderRepository.save(order);
            Invoice invoice = invoiceService.createInvoice(order);
            order.setInvoice(invoice);
            order = orderRepository.save(order);
            orders.add(order);
        }
        preOrder.setOrders(orders);
        return preOrder;
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private Map<String, Set<PreOrderItem>> get(Set<PreOrderItem> preOrderItems) {
        Map<String, Set<PreOrderItem>> sellerPreOrders = new LinkedHashMap<>();

        for(PreOrderItem preOrderItem : preOrderItems) {
            Set<PreOrderItem> sellerPreOrderItems = sellerPreOrders.getOrDefault(preOrderItem.getSellerId(), new LinkedHashSet<>());
            sellerPreOrderItems.add(preOrderItem);
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
        return orderItemRepository.save(orderItem);

    }

}
