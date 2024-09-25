package org.example.bookshop.dto.mail;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailDto {
    private String subject;
    private String message;
    private String to;
}
