package com.academy.projects.ecommerce.ordermanagementservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "invoices")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Invoice extends BaseModel {

    private String invoiceNumber;
    private Date invoiceDate;
    private String customerId;

    @OneToOne(mappedBy = "invoice")
    private Order order;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    @OneToOne
    private PaymentDetails paymentDetails;
}
