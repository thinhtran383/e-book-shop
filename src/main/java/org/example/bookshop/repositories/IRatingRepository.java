package org.example.bookshop.repositories;

import org.example.bookshop.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRatingRepository extends JpaRepository<Rating, Integer> {
}