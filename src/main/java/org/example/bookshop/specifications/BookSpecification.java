package org.example.bookshop.specifications;

import org.example.bookshop.domain.database.Book;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class BookSpecification {

    public static Specification<Book> hasCategory(Integer category) {
        return (root, query, criteriaBuilder) -> {
            if (category == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("categoryID").get("id"), category);
        };
    }

    public static Specification<Book> hasPriceGreaterThan(BigDecimal priceMin) {
        return (root, query, criteriaBuilder) -> {
            if (priceMin == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), priceMin);
        };
    }

    public static Specification<Book> hasPriceLessThan(BigDecimal priceMax) {
        return (root, query, criteriaBuilder) -> {
            if (priceMax == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("price"), priceMax);
        };
    }

    public static Specification<Book> hasPublisher(String publisher) {
        return (root, query, criteriaBuilder) -> {
            if (publisher == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("publisher"), publisher);
        };
    }

    public static Specification<Book> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("title"), "%" + title + "%");
        };
    }



}
