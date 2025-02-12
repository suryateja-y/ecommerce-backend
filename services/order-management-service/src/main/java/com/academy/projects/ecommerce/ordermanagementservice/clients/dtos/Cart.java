package com.academy.projects.ecommerce.ordermanagementservice.clients.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart implements Serializable {
    private String userId;
    private Set<CartUnit> cartUnits = new LinkedHashSet<>();
}
