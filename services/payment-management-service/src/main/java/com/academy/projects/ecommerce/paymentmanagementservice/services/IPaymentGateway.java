package com.academy.projects.ecommerce.paymentmanagementservice.services;

import com.academy.projects.ecommerce.paymentmanagementservice.dtos.ActionGatewayResponse;
import com.academy.projects.ecommerce.paymentmanagementservice.dtos.CancelRequestDto;
import com.academy.projects.ecommerce.paymentmanagementservice.dtos.PaymentInitiationRequest;
import com.academy.projects.ecommerce.paymentmanagementservice.dtos.RefundRequestDto;
import com.academy.projects.ecommerce.paymentmanagementservice.kafka.dtos.PaymentGatewayResponse;

public interface IPaymentGateway {
    PaymentGatewayResponse initiatePayment(PaymentInitiationRequest paymentInitiationRequest);
    ActionGatewayResponse cancel(CancelRequestDto from);
    ActionGatewayResponse refund(RefundRequestDto refundRequest);
}
