package org.example.bookshop.repositories;

import org.example.bookshop.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, Integer> {
}