package org.example.bookshop.repositories;

import org.example.bookshop.entities.User;
import org.example.bookshop.responses.users.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    @Query("SELECT new org.example.bookshop.responses.users.UserResponse(u.id, u.username, u.email, c.fullName, c.phone, c.address) " +
            "FROM User u JOIN u.customers c JOIN u.role r " +
            "WHERE r.roleName = 'customer'")
    Page<UserResponse> findAllUserDetails(Pageable pageable);


}