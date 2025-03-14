package com.academy.projects.ecommerce.ordermanagementservice.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
public class PreOrder extends BaseModel {
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<PreOrderItem> preOrderItems = new LinkedHashSet<>();

    private String customerId;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private PreOrderStatus orderStatus;

    @OneToOne(fetch = FetchType.EAGER)
    private PaymentDetails paymentDetails;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "preOrder")
    private List<Order> orders = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Invoice invoice;

    private String shippingAddressId;

}
