package org.example.bookshop.repositories;

import org.example.bookshop.entities.Customer;
import org.example.bookshop.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByUserID(User user);
}