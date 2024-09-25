package org.example.bookshop.services;

import jakarta.annotation.PreDestroy;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.example.bookshop.dto.mail.MailDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class MailService {
    @Value("${spring.mail.username}")
    private String fromMail;

    private final JavaMailSender mailSender;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public void sendMail(String mail, MailDto mailStructure) {
        executorService.submit(() -> {
            try {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setFrom(fromMail);
                helper.setTo(mail);
                helper.setSubject(mailStructure.getSubject());

                String htmlContent = getHtmlContent("123");
                helper.setText(htmlContent, true);

                mailSender.send(mimeMessage);
            } catch (IOException | MessagingException e) {
                e.printStackTrace();
            }
        });
    }

    private String getHtmlContent(String newPassword) throws IOException {
        ClassPathResource resource = new ClassPathResource("template/index.html");
        InputStream inputStream = resource.getInputStream();
        String htmlTemplate = new String(FileCopyUtils.copyToByteArray(inputStream), StandardCharsets.UTF_8);



        return htmlTemplate.replace("{{newPassword}}", newPassword);
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
    }
}
