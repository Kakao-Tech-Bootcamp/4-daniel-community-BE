package com.daniel.community.post.service;

import com.daniel.community.post.dto.PostImageUploadRequest;
import com.daniel.community.post.dto.PostImageUploadResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.UUID;

@Service
public class PostImageService {

    private static final String UPLOAD_DIRECTORY = "uploads/posts";
    private static final String IMAGE_URL_PREFIX = "/images/posts/";

    public PostImageUploadResponse uploadPostImage(PostImageUploadRequest request) {
        String extension = getExtension(request.getPostImageName());
        String savedFileName = UUID.randomUUID() + "." + extension;

        try {
            Path uploadPath = Path.of(UPLOAD_DIRECTORY);
            Files.createDirectories(uploadPath);

            byte[] imageBytes = decodeBase64Image(request.getPostImageData());

            Path filePath = uploadPath.resolve(savedFileName);
            Files.write(filePath, imageBytes);

            return new PostImageUploadResponse(IMAGE_URL_PREFIX + savedFileName);
        } catch (IOException exception) {
            throw new IllegalArgumentException("internal_server_error");
        }
    }

    private String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");

        if (dotIndex == -1) {
            throw new IllegalArgumentException("invalid_request");
        }

        String extension = fileName.substring(dotIndex + 1).toLowerCase();

        if (!isAllowedExtension(extension)) {
            throw new IllegalArgumentException("invalid_request");
        }

        return extension;
    }

    private boolean isAllowedExtension(String extension) {
        return extension.equals("png")
                || extension.equals("jpg")
                || extension.equals("jpeg")
                || extension.equals("webp");
    }

    private byte[] decodeBase64Image(String imageData) {
        String[] parts = imageData.split(",");

        if (parts.length != 2) {
            throw new IllegalArgumentException("invalid_request");
        }

        return Base64.getDecoder().decode(parts[1]);
    }
}