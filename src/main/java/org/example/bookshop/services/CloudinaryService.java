package org.example.bookshop.services;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {
    private final Cloudinary cloudinary;


    public String upload(MultipartFile file) {
        try {
            Map<String, String> data = cloudinary.uploader().upload(file.getBytes(), Map.of());
            String url = data.get("secure_url");
            return url;
        } catch (IOException io) {
            throw new RuntimeException("Image upload fail");
        }
    }

}
