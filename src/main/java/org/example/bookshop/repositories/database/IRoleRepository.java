package org.example.bookshop.repositories.database;

import org.example.bookshop.domain.database.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role, Integer> {
}