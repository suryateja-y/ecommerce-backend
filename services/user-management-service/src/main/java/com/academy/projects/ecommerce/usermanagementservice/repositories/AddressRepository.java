package com.academy.projects.ecommerce.usermanagementservice.repositories;

import com.academy.projects.ecommerce.usermanagementservice.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
}
