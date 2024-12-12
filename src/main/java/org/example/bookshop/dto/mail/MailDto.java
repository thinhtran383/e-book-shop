package org.example.bookshop.dto.mail;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailDto {
    private String subject;
    private String to;

    private Map<String, Object> placeholders;
    private String templateName;
}
