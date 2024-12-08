package org.example.bookshop.repositories;

import org.example.bookshop.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ICategoryRepository extends JpaRepository<Category, Integer> {
}