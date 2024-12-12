package org.example.bookshop.domain.database;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Customers", schema = "book_shop")
@ToString
public class Customer {
    @Id
    @Column(name = "CustomerID", nullable = false)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "userID", nullable = false)
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


    @PrePersist
    public void prePersist(){
        this.id = (int) (Math.random() * 1000);

        createdDate = LocalDate.now();
    }

}