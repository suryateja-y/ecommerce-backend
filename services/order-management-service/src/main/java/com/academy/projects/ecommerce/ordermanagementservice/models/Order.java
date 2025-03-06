package com.academy.projects.ecommerce.ordermanagementservice.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order extends BaseModel {

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems = new LinkedHashSet<>();

    private String customerId;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PaymentDetails paymentDetails;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PreOrder preOrder;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TrackingDetails trackingDetails;
}
