package com.academy.projects.ecommerce.ordermanagementservice.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "preorders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PreOrder extends BaseModel {
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<PreOrderItem> preOrderItems = new LinkedHashSet<>();

    private String customerId;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private PreOrderStatus orderStatus;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PaymentDetails paymentDetails;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "preOrder")
    private List<Order> orders = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Invoice invoice;
}
