package org.example.bookshop.dto.user;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExistUserDto {
    private String email;
    private String phone;
}
