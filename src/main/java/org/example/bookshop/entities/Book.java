package org.example.bookshop.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Books", schema = "book_shop")
public class Book {
    @Id
    @Column(name = "BookID", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "Title", nullable = false)
    private String title;

    @Size(max = 255)
    @Column(name = "Author")
    private String author;

    @NotNull
    @Column(name = "Price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull
    @Column(name = "Quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "CategoryID")
    private Category categoryID;

    @Lob
    @Column(name = "Description")
    private String description;

    @Size(max = 255)
    @Column(name = "Publisher")
    private String publisher;

    @Column(name = "PublishedDate")
    private LocalDate publishedDate;

    @Size(max = 255)
    @Column(name = "Image")
    private String image;

    @Column(name = "AverageRating", precision = 2, scale = 1)
    private BigDecimal averageRating;

    @OneToMany(mappedBy = "bookID")
    private Set<Rating> ratings = new LinkedHashSet<>();


    @PrePersist
    public void prePersist() {
        this.id = (int) (Math.random() * 1000);
    }

}