package com.academy.projects.ecommerce.usermanagementservice.repositories;

import com.academy.projects.ecommerce.usermanagementservice.models.Customer;
import com.academy.projects.ecommerce.usermanagementservice.models.UserState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Page<Customer> findAllByUser_UserState(UserState userState, Pageable pageable);
}
