package org.example.bookshop.repositories.database;

import org.example.bookshop.domain.database.Customer;
import org.example.bookshop.domain.database.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByUserID(User user);
}