package com.academy.projects.ecommerce.paymentmanagementservice.repositories;

import com.academy.projects.ecommerce.paymentmanagementservice.models.Payment;
import com.academy.projects.ecommerce.paymentmanagementservice.models.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    boolean existsByCustomerIdAndPaymentStatus(String customerId, PaymentStatus paymentStatus);
    Page<Payment> findAllByCustomerIdAndOrderIdAndPaymentStatus(String customerId, String orderId, PaymentStatus paymentStatus, Pageable pageable);
    Page<Payment> findAllByCustomerIdAndOrderId(String customerId, String orderId, Pageable pageable);
    Page<Payment> findAllByCustomerIdAndPaymentStatus(String customerId, PaymentStatus paymentStatus, Pageable pageable);
    Page<Payment> findAllByCustomerId(String customerId, Pageable pageable);
    Optional<Payment> findByOrderIdAndPaymentStatus(String orderId, PaymentStatus paymentStatus);
}
