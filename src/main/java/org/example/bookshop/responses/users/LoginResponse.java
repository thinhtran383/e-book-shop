package org.example.bookshop.responses.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String username;
    private String fullName;
    private String email;
    private String address;
    private String phone;
}
