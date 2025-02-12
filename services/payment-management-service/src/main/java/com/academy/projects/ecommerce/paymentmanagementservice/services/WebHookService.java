package com.academy.projects.ecommerce.paymentmanagementservice.services;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.academy.projects.ecommerce.paymentmanagementservice.exceptions.UnhandledEventTypeException;
import com.academy.projects.ecommerce.paymentmanagementservice.kafka.dtos.PaymentIntentDto;
import com.academy.projects.ecommerce.paymentmanagementservice.models.PaymentMethod;
import com.academy.projects.ecommerce.paymentmanagementservice.models.PaymentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WebHookService implements IWebHookService {

    @Value("${application.gateway.webhook-secret}")
    private String webHookSecret;

    private final IPaymentService paymentService;

    private final Logger logger = LoggerFactory.getLogger(WebHookService.class);

    @Autowired
    public WebHookService(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public void handleEvent(String payload, String signature) {
        try {
            logger.info("Received webhook event: {}", payload);
            // Verify the Signature to check the authenticity
            Event event = Webhook.constructEvent(payload, signature, webHookSecret);
            PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
            PaymentStatus paymentStatus = switch (event.getType()) {
                case "payment_intent.succeeded" -> PaymentStatus.PAID;
                case "payment_intent.failed" -> PaymentStatus.FAILED;
                default -> throw new UnhandledEventTypeException(event.getType());
            };
            if(paymentIntent != null)
                paymentService.update(from(paymentIntent, paymentStatus));
        } catch (SignatureVerificationException e) {
            logger.error(e.getMessage());
        }

    }

    private PaymentIntentDto from(PaymentIntent paymentIntent, PaymentStatus paymentStatus) {
        return PaymentIntentDto.builder()
                .paymentId(paymentIntent.getId())
                .paymentStatus(paymentStatus)
                .paymentMethod(PaymentMethod.valueOf(paymentIntent.getPaymentMethod()))
                .reason((paymentIntent.getCancellationReason() == null ? (paymentIntent.getLastPaymentError() != null) ? paymentIntent.getLastPaymentError().getMessage() : "" : paymentIntent.getCancellationReason()))
                .build();
    }
}
