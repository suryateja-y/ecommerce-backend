package com.academy.projects.ecommerce.paymentmanagementservice.controllers;

import com.academy.projects.ecommerce.paymentmanagementservice.exceptions.NoActivePaymentException;
import com.academy.projects.ecommerce.paymentmanagementservice.models.Payment;
import com.academy.projects.ecommerce.paymentmanagementservice.models.PaymentStatus;
import com.academy.projects.ecommerce.paymentmanagementservice.services.PaymentService;
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
@RequestMapping("/api/${application.version}/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    public PaymentController(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/{orderId}/activePayment")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<Payment> getPayment(@PathVariable String orderId, Authentication authentication) {
        List<Payment> payments = paymentService.get(authentication.getName(), orderId, PaymentStatus.UNPAID, 0, 10);
        if(payments.isEmpty()) throw new NoActivePaymentException(authentication.getName(), orderId);
        return ResponseEntity.ok(payments.get(0));
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<List<Payment>> getPayments(Authentication authentication, @RequestParam(required = false) String orderId, @RequestParam(required = false) PaymentStatus paymentStatus, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int pageSize) {
        List<Payment> payments = paymentService.get(authentication.getName(), orderId, paymentStatus, page, pageSize);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_PAYMENT_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<Payment>> getPayments(@RequestParam(required = false) String customerId, @RequestParam(required = false) String orderId, @RequestParam(required = false) PaymentStatus paymentStatus, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int pageSize) {
        List<Payment> payments = paymentService.get(customerId, orderId, paymentStatus, page, pageSize);
        return ResponseEntity.ok(payments);
    }

    @ExceptionHandler(NoActivePaymentException.class)
    public ResponseEntity<String> handleNoActivePaymentException(final NoActivePaymentException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(final RuntimeException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
