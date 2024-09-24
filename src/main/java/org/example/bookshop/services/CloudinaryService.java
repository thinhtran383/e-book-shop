package org.example.bookshop.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {
    private final Cloudinary cloudinary;

    // destroy image
    public Mono<Map> destroyImage(String publicId) {
        return Mono.fromCallable(() -> {
            Map destroyResult = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("Destroy image result: {}", destroyResult);
            return destroyResult;
        });
    }

    public Mono<Map> uploadFile(MultipartFile multipartFile) {
        return Mono.fromCallable(() -> {
            File file = convertToFile(multipartFile);
            try {
                Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
                file.delete();
                String imageUrl = (String) uploadResult.get("url");
                log.info("Image URL: {}", imageUrl);

                return uploadResult;
            } catch (IOException e) {
                throw new RuntimeException("Upload failed", e);
            }
        });
    }


    private File convertToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }
        return file;
    }
}
