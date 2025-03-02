package com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos;

import com.academy.projects.ecommerce.ordermanagementservice.models.PreOrderItem;
import com.academy.projects.ecommerce.ordermanagementservice.models.PreOrderStatus;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreOrderDto implements Serializable {
    private String preOrderId;
    private Date preOrderDate;
    private Set<PreOrderItem> preOrderItems;
    private String customerId;
    private BigDecimal totalAmount;
    private PreOrderStatus orderStatus;
    private String invoiceId;
    private Action action;
}
