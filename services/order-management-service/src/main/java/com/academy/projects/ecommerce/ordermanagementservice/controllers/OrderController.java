package com.academy.projects.ecommerce.ordermanagementservice.controllers;

import com.academy.projects.ecommerce.ordermanagementservice.dtos.CreateOrderResponseDto;
import com.academy.projects.ecommerce.ordermanagementservice.dtos.UpdateOrderStatusRequestDto;
import com.academy.projects.ecommerce.ordermanagementservice.models.Order;
import com.academy.projects.ecommerce.ordermanagementservice.models.OrderStatus;
import com.academy.projects.ecommerce.ordermanagementservice.services.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/${application.version}/orders")
public class OrderController {

    private final IOrderService orderService;

    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    public OrderController(final IOrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('ROLE_ORDER_MANAGER', 'ROLE_ADMIN') or ( hasRole('ROLE_CUSTOMER') and @accessChecker.isOwner(#customerId) )")
    public ResponseEntity<List<Order>> getOrders(@PathVariable String customerId, @RequestParam(required = false) OrderStatus orderStatus, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int pageSize) {
        List<Order> orders = orderService.getOrders(customerId, orderStatus, page, pageSize);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PatchMapping("/{orderId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<CreateOrderResponseDto> updateOrder(@RequestBody UpdateOrderStatusRequestDto requestDto, Authentication authentication) {
        Order order = orderService.updateStatus(authentication.getName(), requestDto.getOrderId(), requestDto.getOrderStatus());
        CreateOrderResponseDto responseDto = new CreateOrderResponseDto();
        responseDto.setOrderId(order.getId());
        responseDto.setOrderStatus(order.getOrderStatus());
        return new ResponseEntity<>(responseDto, HttpStatus.ACCEPTED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(final RuntimeException e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
