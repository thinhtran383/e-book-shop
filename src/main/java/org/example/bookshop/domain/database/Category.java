package org.example.bookshop.domain.database;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Categories", schema = "book_shop")
public class Category {
    @Id
    @Column(name = "CategoryID", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "CategoryName", nullable = false)
    private String categoryName;

    @Lob
    @Column(name = "Description")
    private String description;

    @PrePersist
    public void prePersist() {
        this.id = (int) (Math.random() * 10000);
    }

}