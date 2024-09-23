package org.example.bookshop.repositories;

import org.example.bookshop.dto.ExistUserDto;
import org.example.bookshop.entities.User;
import org.example.bookshop.responses.users.LoginResponse;
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

    @Query("""
            SELECT new org.example.bookshop.responses.users.UserResponse(u.id, u.username, u.email, c.fullName, c.phone, c.address)
            FROM User u 
            JOIN u.customer c 
            JOIN u.role r 
            WHERE r.roleName = 'customer'
            """)
    Page<UserResponse> findAllUserDetails(Pageable pageable);

    @Query("""
            SELECT COUNT(u) > 0
            FROM User u
            JOIN u.customer c
            WHERE u.email = :email OR c.phone = :phone
            """)
    boolean existsByEmailOrPhone(@Param("email") String email, @Param("phone") String phone);


//    @Query("select new org.example.bookshop.responses.users.LoginResponse(u.username, c.fullName, u.email, c.address, c.phone, r.roleName)" +
//            "from User u join u.customers c join u.role r" +
//            " where u.username = :username")
//    Optional<LoginResponse> findUserWithCustomerDetails(@Param("username") String username);
}