package com.academy.projects.ecommerce.ordermanagementservice.repositories;

import com.academy.projects.ecommerce.ordermanagementservice.models.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, String> {
}
