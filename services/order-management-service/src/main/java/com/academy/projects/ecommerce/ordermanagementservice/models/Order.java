package com.academy.projects.ecommerce.ordermanagementservice.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Transactional
public class Order extends BaseModel {

    @OneToMany(fetch = FetchType.EAGER)
    private Set<OrderItem> orderItems = new LinkedHashSet<>();

    private String customerId;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToOne(fetch = FetchType.EAGER)
    private PaymentDetails paymentDetails;

    @OneToOne(fetch = FetchType.LAZY)
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.EAGER)
    private PreOrder preOrder;

    @OneToOne(fetch = FetchType.EAGER)
    private TrackingDetails trackingDetails;
}
