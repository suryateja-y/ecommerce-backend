package com.academy.projects.ecommerce.ordermanagementservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity(name = "delivery_feasibility_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryFeasibility implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Date eta;
    private long etaInHours;
    @Builder.Default
    private Boolean isFeasible = false;
    private String reason;
    private String sellerId;
}
