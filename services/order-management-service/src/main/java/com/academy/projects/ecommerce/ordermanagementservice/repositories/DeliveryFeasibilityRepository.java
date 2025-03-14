package com.academy.projects.ecommerce.ordermanagementservice.repositories;

import com.academy.projects.ecommerce.ordermanagementservice.models.DeliveryFeasibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryFeasibilityRepository extends JpaRepository<DeliveryFeasibility, String> {
}
