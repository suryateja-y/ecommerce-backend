package com.academy.projects.ecommerce.ordermanagementservice.repositories;

import com.academy.projects.ecommerce.ordermanagementservice.models.Order;
import com.academy.projects.ecommerce.ordermanagementservice.models.OrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findAllByCustomerIdAndOrderStatus(String customerId, OrderStatus orderStatus, Pageable pageable);
    List<Order> findAllByCustomerId(String customerId);
    boolean existsByCustomerIdAndOrderStatus(String customerId, OrderStatus orderStatus);
    Optional<Order> findByIdAndCustomerId(String orderId, String customerId);
}
