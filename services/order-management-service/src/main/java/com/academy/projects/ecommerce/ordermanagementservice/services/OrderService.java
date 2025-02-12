package com.academy.projects.ecommerce.ordermanagementservice.services;

import com.academy.projects.ecommerce.ordermanagementservice.clients.services.ICartToOrderService;
import com.academy.projects.ecommerce.ordermanagementservice.dtos.UpdateOrderRequestDto;
import com.academy.projects.ecommerce.ordermanagementservice.exceptions.InvalidStatusRequest;
import com.academy.projects.ecommerce.ordermanagementservice.exceptions.OrderNotFoundException;
import com.academy.projects.ecommerce.ordermanagementservice.exceptions.PendingOrderExistsException;
import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.Action;
import com.academy.projects.ecommerce.ordermanagementservice.kafka.producers.services.IOrderUpdateManager;
import com.academy.projects.ecommerce.ordermanagementservice.models.*;
import com.academy.projects.ecommerce.ordermanagementservice.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
public class OrderService implements IOrderService {

    private final ICartToOrderService cartToOrderService;
    private final OrderRepository orderRepository;
    private final IOrderUpdateManager orderUpdateManager;
    private final IInvoiceService invoiceService;
    private final IInventoryService inventoryService;

    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    public OrderService(ICartToOrderService cartToOrderService, OrderRepository orderRepository, IOrderUpdateManager orderUpdateManager, InvoiceService invoiceService, IInventoryService inventoryService) {
        this.cartToOrderService = cartToOrderService;
        this.orderRepository = orderRepository;
        this.orderUpdateManager = orderUpdateManager;
        this.invoiceService = invoiceService;
        this.inventoryService = inventoryService;
    }

    @Override
    public Order checkout(String customerId) {
        if(orderRepository.existsByCustomerIdAndOrderStatus(customerId, OrderStatus.PENDING_FOR_PAYMENT)) throw new PendingOrderExistsException(customerId);
        Set<OrderItem> orderItems = cartToOrderService.getOrderItems();

        // Request Inventory Service to Block the Inventory
        inventoryService.block(orderItems);

        Order order = createOrder(customerId, orderItems);

        // Sending update to Kafka
        orderUpdateManager.sendUpdate(order, Action.CREATE);

        logger.info("Order {} created successfully!!!", order);
        return order;
    }

    @Override
    public List<Order> getOrders(String customerId, OrderStatus orderStatus, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        if(orderStatus != null) return orderRepository.findAllByCustomerIdAndOrderStatus(customerId, orderStatus, pageable);
        else return orderRepository.findAllByCustomerId(customerId);
    }

    @Override
    public Order update(UpdateOrderRequestDto requestDto) {
        Order order = orderRepository.findById(requestDto.getOrderId()).orElseThrow(() -> new OrderNotFoundException(requestDto.getOrderId()));
        PaymentStatus paymentStatus = PaymentStatus.IN_PROCESSING;
        if(requestDto.getPaymentDetails() != null) {
            order.setPaymentDetails(requestDto.getPaymentDetails());
            if(requestDto.getPaymentDetails().getPaymentStatus() != null)
                paymentStatus = requestDto.getPaymentDetails().getPaymentStatus();
        }

        if(order.getOrderStatus().equals(OrderStatus.CANCELLED)) {
            if(paymentStatus.equals(PaymentStatus.PAID)) {
                // Sent Payment Service to Refund
                orderUpdateManager.sendUpdate(order, Action.REFUND);
            }
        } else {
            order.setOrderStatus(from(paymentStatus));
            if(order.getOrderStatus().equals(OrderStatus.CANCELLED)) {
                // Request Inventory Service to Release the Inventory
                inventoryService.release(order.getOrderItems());

                // Sending Update to Kafka
                orderUpdateManager.sendUpdate(order, Action.CANCELLED);
            } else if(order.getOrderStatus().equals(OrderStatus.CREATED)) {

                Invoice invoice = invoiceService.createInvoice(order);
                order.setInvoice(invoice);
                // Send Order created notification to the user
                orderUpdateManager.sendUpdate(order, Action.CREATE);
            }
        }

        order = orderRepository.save(order);
        logger.info("Order {} updated successfully!!!", order);
        return order;
    }

    @Override
    public Order updateStatus(String customerId, String orderId, OrderStatus orderStatus) {
        Order order = orderRepository.findByIdAndCustomerId(orderId, customerId).orElseThrow(() -> new OrderNotFoundException(orderId, customerId));
        if(orderStatus.equals(OrderStatus.CANCELLED)) {
            if(order.getOrderStatus().equals(OrderStatus.CANCELLED)) return order;
            if(order.getOrderStatus().equals(OrderStatus.PENDING_FOR_PAYMENT) || order.getOrderStatus().equals(OrderStatus.CREATED)) {
                // Send Inventory Service to add the inventory
                // Implement it after implementing Service Mesh
                order.setOrderStatus(OrderStatus.CANCELLED);
                order = orderRepository.save(order);

                // Send details to Payment Service to stop the payment
                orderUpdateManager.sendUpdate(order, Action.CANCELLED);
            } else throw new InvalidStatusRequest("Order is already completed!!! Can not cancel now!!!");
        }
        logger.info("Order '{}' status updated to '{}' successfully!!!", order.getId(), order.getOrderStatus());
        return order;
    }

    private Order createOrder(String customerId, Set<OrderItem> orderItems) {
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setOrderItems(orderItems);
        order.setOrderStatus(OrderStatus.PENDING_FOR_PAYMENT);
        order.setTotalAmount(getTotalAmount(orderItems));
        return orderRepository.save(order);
    }

    private BigDecimal getTotalAmount(Set<OrderItem> orderItems) {
        return orderItems.stream().map(OrderItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private OrderStatus from(PaymentStatus paymentStatus) {
        return switch (paymentStatus) {
            case PAID -> OrderStatus.CREATED;
            case CANCELLED, FAILED -> OrderStatus.CANCELLED;
            default -> OrderStatus.PENDING_FOR_PAYMENT;
        };
    }
}
