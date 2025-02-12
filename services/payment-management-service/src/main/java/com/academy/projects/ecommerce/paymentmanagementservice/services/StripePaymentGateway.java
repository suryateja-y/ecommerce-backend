package com.academy.projects.ecommerce.paymentmanagementservice.services;

import com.academy.projects.ecommerce.paymentmanagementservice.dtos.*;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCancelParams;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.academy.projects.ecommerce.paymentmanagementservice.kafka.dtos.OrderItem;
import com.academy.projects.ecommerce.paymentmanagementservice.kafka.dtos.PaymentGatewayResponse;
import com.academy.projects.ecommerce.paymentmanagementservice.models.PaymentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


@Service
public class StripePaymentGateway implements IPaymentGateway {

    @Value("${application.gateway.secret-key}")
    private String stripeSecret;

    @Value("${application.gateway.successUrl}")
    private String successUrl;

    @Value("${application.gateway.cancelUrl}")
    private String cancelUrl;

    @Value("${application.gateway.currency}")
    private String currency;

    private final Logger logger = LoggerFactory.getLogger(StripePaymentGateway.class);

    @Override
    public PaymentGatewayResponse initiatePayment(PaymentInitiationRequest paymentInitiationRequest) {
        // Setting Stripe API Secret
        Stripe.apiKey = stripeSecret;

        // Line items
        List<SessionCreateParams.LineItem> lineItems = new LinkedList<>();
        for(OrderItem orderItem : paymentInitiationRequest.getOrderItems())
            lineItems.add(createLineItem(orderItem));

        Session session;
        try {
            session = Session.create(createParams(paymentInitiationRequest.getCustomerId(), lineItems));
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

        PaymentGatewayResponse paymentGatewayResponse = PaymentGatewayResponse.builder()

                .paymentId(session.getPaymentIntent())
                .paymentStatus(PaymentStatus.UNPAID)
                .paymentUrl(session.getUrl())
                .build();
        logger.info("Payment Request sent to Stripe. Payment Id: '{}'!!!", session.getId());
        return paymentGatewayResponse;
    }

    @Override
    public ActionGatewayResponse cancel(CancelRequestDto cancelRequest) {
        ActionStatus actionStatus = ActionStatus.FAILURE;
        Date actedAt = new Date();
        String reason = "Failed to cancel the request. Issue from Stripe, returned empty Payment Intent";
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(cancelRequest.getPaymentId());
            PaymentIntentCancelParams cancelParams = PaymentIntentCancelParams.builder().setCancellationReason(PaymentIntentCancelParams.CancellationReason.REQUESTED_BY_CUSTOMER).build();
            PaymentIntent cancelledIntent = paymentIntent.cancel(cancelParams);

            if((cancelledIntent != null) && (cancelledIntent.getStatus().equalsIgnoreCase("canceled"))) {
                actedAt = from(cancelledIntent.getCanceledAt());
                actionStatus = ActionStatus.SUCCESS;
                reason = "Cancelled";
            }
        } catch (StripeException e) {
            reason = e.getMessage();
        }

        return ActionGatewayResponse.builder().paymentId(cancelRequest.getPaymentId()).actionStatus(actionStatus).actedAt(actedAt).reason(reason).build();

    }

    @Override
    public ActionGatewayResponse refund(RefundRequestDto refundRequest) {
        ActionStatus actionStatus = ActionStatus.FAILURE;
        Date actedAt = new Date();
        String reason = "Failed to refund. Issue from Stripe, returned empty Payment Intent";
        try {
            RefundCreateParams createParams = RefundCreateParams.builder().setPaymentIntent(refundRequest.getPaymentId()).setReason(RefundCreateParams.Reason.REQUESTED_BY_CUSTOMER).build();
            Refund refund = Refund.create(createParams);
            if((refund != null) && refund.getStatus().equalsIgnoreCase("refunded")) {
                actedAt = from(refund.getCreated());
                reason = "Refunded";
                actionStatus = ActionStatus.SUCCESS;
            }
        } catch (StripeException e) {
            reason = e.getMessage();
        }
        return ActionGatewayResponse.builder().paymentId(refundRequest.getPaymentId()).actionStatus(actionStatus).reason(reason).actedAt(actedAt).build();
    }

    private Date from(Long seconds) {
        return new Date(seconds * 1000);
    }

    private SessionCreateParams.LineItem createLineItem(OrderItem orderItem) {
        SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(orderItem.getProductName())
                .setDescription(orderItem.getProductName())
                .build();
        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                .setProductData(productData)
                .setCurrency(currency)
                .setUnitAmountDecimal(orderItem.getUnitPrice().multiply(BigDecimal.TEN))
                .build();
        return SessionCreateParams.LineItem.builder()
                .setPriceData(priceData)
                .setQuantity(orderItem.getQuantity())
                .build();
    }

    private SessionCreateParams createParams(String customerId, List<SessionCreateParams.LineItem> lineItems) {
        return SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.AMAZON_PAY)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCustomer(customerId)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addAllLineItem(lineItems)
                .build();
    }
}
