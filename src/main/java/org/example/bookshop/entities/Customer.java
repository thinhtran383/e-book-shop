package org.example.bookshop.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Customers", schema = "book_shop")
public class Customer {
    @Id
    @Column(name = "CustomerID", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UserID", nullable = false)
    private User userID;

    @Size(max = 255)
    @Column(name = "FullName")
    private String fullName;

    @Size(max = 50)
    @Column(name = "Phone", length = 50)
    private String phone;

    @Size(max = 255)
    @Column(name = "Address")
    private String address;

    @Column(name = "CreatedDate")
    private LocalDate createdDate;

}