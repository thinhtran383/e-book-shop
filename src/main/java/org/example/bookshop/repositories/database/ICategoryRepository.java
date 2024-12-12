package org.example.bookshop.repositories.database;

import org.example.bookshop.domain.database.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<Category, Integer> {
}