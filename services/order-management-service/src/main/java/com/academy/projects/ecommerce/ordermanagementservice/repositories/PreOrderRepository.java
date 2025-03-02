package com.academy.projects.ecommerce.ordermanagementservice.repositories;

import com.academy.projects.ecommerce.ordermanagementservice.models.PreOrder;
import com.academy.projects.ecommerce.ordermanagementservice.models.PreOrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreOrderRepository extends JpaRepository<PreOrder, String> {
    boolean existsByCustomerIdAndOrderStatus(String customerId, PreOrderStatus orderStatus);
    List<PreOrder> findAllByCustomerIdAndOrderStatus(String customerId, PreOrderStatus preOrderStatus, Pageable pageable);
    List<PreOrder> findAllByCustomerId(String customerId);
}
