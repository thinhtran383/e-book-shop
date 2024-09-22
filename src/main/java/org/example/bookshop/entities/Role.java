package org.example.bookshop.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Roles", schema = "book_shop")
public class Role {
    @Id
    @Column(name = "RoleID", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "RoleName", nullable = false)
    private String roleName;

}