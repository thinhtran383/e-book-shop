package org.example.bookshop.dto.user;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserDto {
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email is invalid")
    private String email;
    private String password;
    private String fullName;
    private String address;

    @Pattern(regexp = "^(\\+84|0)\\d{9,10}$", message = "Phone is invalid")
    private String phone;
}
