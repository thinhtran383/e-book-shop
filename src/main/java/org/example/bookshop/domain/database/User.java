package org.example.bookshop.domain.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Users", schema = "book_shop")
public class User implements UserDetails {
    @Id
    @Column(name = "UserID", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "Username", nullable = false)
    private String username;

    @Size(max = 255)
    @NotNull
    @Column(name = "Password", nullable = false)
    private String password;

    @Size(max = 255)
    @Column(name = "Email")
    private String email;

    @NotNull
    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "RoleID", nullable = false)
    private Role role;

    @JsonIgnore
    @OneToOne(mappedBy = "userID", cascade = CascadeType.ALL, optional = false)
    private Customer customer;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + getRole().getRoleName().toUpperCase()));
    }

    @PrePersist
    public void prePersist() {
        this.id = (int) (Math.random() * 1000);
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}