package com.academy.projects.ecommerce.paymentmanagementservice.services;

import com.academy.projects.ecommerce.paymentmanagementservice.dtos.*;
import com.academy.projects.ecommerce.paymentmanagementservice.exceptions.*;
import com.academy.projects.ecommerce.paymentmanagementservice.kafka.dtos.Action;
import com.academy.projects.ecommerce.paymentmanagementservice.kafka.dtos.PaymentGatewayResponse;
import com.academy.projects.ecommerce.paymentmanagementservice.kafka.dtos.PaymentIntentDto;
import com.academy.projects.ecommerce.paymentmanagementservice.kafka.producers.services.PaymentUpdateManager;
import com.academy.projects.ecommerce.paymentmanagementservice.models.Payment;
import com.academy.projects.ecommerce.paymentmanagementservice.models.PaymentStatus;
import com.academy.projects.ecommerce.paymentmanagementservice.repositories.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PaymentService implements IPaymentService {

    private final IPaymentGateway paymentGateway;
    private final PaymentRepository paymentRepository;
    private final PaymentUpdateManager paymentUpdateManager;

    private final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    public PaymentService(IPaymentGateway paymentGateway, PaymentRepository paymentRepository, PaymentUpdateManager paymentUpdateManager) {
        this.paymentGateway = paymentGateway;
        this.paymentRepository = paymentRepository;
        this.paymentUpdateManager = paymentUpdateManager;
    }

    @Override
    public Payment initiatePayment(PaymentInitiationRequest paymentInitiationRequest) {
        logger.info("Received Payment Request for the order: '{}'!!!", paymentInitiationRequest.getOrderId());
        if(paymentRepository.existsByCustomerIdAndPaymentStatus(paymentInitiationRequest.getCustomerId(), PaymentStatus.UNPAID)) throw new PendingPaymentException(paymentInitiationRequest.getCustomerId());
        PaymentGatewayResponse gatewayResponse = paymentGateway.initiatePayment(paymentInitiationRequest);
        Payment payment = paymentRepository.save(from(gatewayResponse, paymentInitiationRequest));
        logger.info("Payment successfully initiated for order: '{}'!", payment.getOrderId());
        return payment;
    }

    @Override
    public List<Payment> get(String customerId, String orderId, PaymentStatus paymentStatus, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Payment> paymentPage;

        if(customerId != null) {
            if (orderId != null) {
                if (paymentStatus != null)
                    paymentPage = paymentRepository.findAllByCustomerIdAndOrderIdAndPaymentStatus(customerId, orderId, paymentStatus, pageable);
                else
                    paymentPage = paymentRepository.findAllByCustomerIdAndOrderId(customerId, orderId, pageable);
            } else {
                if (paymentStatus != null) {
                    paymentPage = paymentRepository.findAllByCustomerIdAndPaymentStatus(customerId, paymentStatus, pageable);
                } else {
                    paymentPage = paymentRepository.findAllByCustomerId(customerId, pageable);
                }
            }
        } else paymentPage = paymentRepository.findAll(pageable);
        return paymentPage.getContent();
    }

    @Override
    public void update(PaymentIntentDto paymentIntentDto) {
        String paymentId = paymentIntentDto.getPaymentId();
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));
        payment.setPaymentStatus(paymentIntentDto.getPaymentStatus());
        payment.setPaymentMethod(paymentIntentDto.getPaymentMethod());
        payment.setReason(paymentIntentDto.getReason());
        payment = paymentRepository.save(payment);

        logger.info("Payment successfully updated for order: '{}'!", payment.getOrderId());
        // Send update to the Kafka
        paymentUpdateManager.sendUpdate(payment, Action.STATUS_UPDATE);
    }

    @Override
    public Payment cancel(PaymentInitiationRequest request) {
        Payment payment = paymentRepository.findByOrderIdAndPaymentStatus(request.getOrderId(), PaymentStatus.UNPAID).orElseThrow(() -> new NoUnPaidPaymentFoundException(request.getOrderId()));
        payment.setPaymentStatus(PaymentStatus.CANCELLED);
        String reason = "Order has been cancelled by the user";
        payment.setReason(reason);
        // Send request to Payment Gateway to cancel the payment
        ActionGatewayResponse response = paymentGateway.cancel(getCancelRequest(payment.getPaymentId(), reason));
        if(response.getActionStatus().equals(ActionStatus.SUCCESS)) {
            payment = paymentRepository.save(payment);
            payment.setModifiedAt(response.getActedAt());
            return payment;
        } else throw new CancellationFailedException(payment.getPaymentId(), response.getReason());
    }

    private CancelRequestDto getCancelRequest(String paymentId, String reason) {
        return CancelRequestDto.builder().paymentId(paymentId).reason(reason).build();
    }

    @Override
    public Payment refund(PaymentInitiationRequest request) {
        Payment payment = paymentRepository.findByOrderIdAndPaymentStatus(request.getOrderId(), PaymentStatus.UNPAID).orElseThrow(() -> new NoPaidPaymentFoundException(request.getOrderId()));
        String reason = "Order has been cancelled by the user";

        // Send request to Payment Gateway to refund the payment
        ActionGatewayResponse response = paymentGateway.refund(getRefundRequest(payment.getPaymentId(), reason));
        if(response.getActionStatus().equals(ActionStatus.SUCCESS)) {
            payment.setPaymentStatus(PaymentStatus.REFUNDED);
            payment.setModifiedAt(new Date());
            payment.setReason(reason);
            payment = paymentRepository.save(payment);
            return payment;
        } else throw new RefundFailedException(payment.getPaymentId(), response.getReason());

    }

    private RefundRequestDto getRefundRequest(String paymentId, String reason) {
        return RefundRequestDto.builder().paymentId(paymentId).reason(reason).build();
    }

    private Payment from(PaymentGatewayResponse gatewayResponse, PaymentInitiationRequest paymentInitiationRequest) {
        Payment payment = new Payment();
        payment.setPaymentId(gatewayResponse.getPaymentId());
        payment.setOrderId(paymentInitiationRequest.getOrderId());
        payment.setCustomerId(paymentInitiationRequest.getCustomerId());
        payment.setPaymentId(gatewayResponse.getPaymentId());
        payment.setPaymentStatus(gatewayResponse.getPaymentStatus());
        payment.setTotalAmount(paymentInitiationRequest.getTotalAmount());
        payment.setPaymentUrl(gatewayResponse.getPaymentUrl());
        return payment;
    }
}
