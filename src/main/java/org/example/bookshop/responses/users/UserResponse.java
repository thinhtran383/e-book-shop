package org.example.bookshop.responses.users;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Integer id;
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;



}
