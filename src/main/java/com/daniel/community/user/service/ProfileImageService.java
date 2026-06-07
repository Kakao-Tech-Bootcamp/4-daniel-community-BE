package com.daniel.community.user.service;

import com.daniel.community.user.dto.ProfileImageUploadRequest;
import com.daniel.community.user.dto.ProfileImageUploadResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.UUID;

@Service
public class ProfileImageService {

    private static final String UPLOAD_DIRECTORY = "uploads/profiles";
    private static final String IMAGE_URL_PREFIX = "/images/profiles/";

    public ProfileImageUploadResponse uploadProfileImage(ProfileImageUploadRequest request) {
        String extension = getExtension(request.getProfileImageName());
        // 파일명 충돌을 막기위해 UUID 사용
        String savedFileName = UUID.randomUUID() + "." + extension;

        try {
            Path uploadPath = Path.of(UPLOAD_DIRECTORY);
            Files.createDirectories(uploadPath);

            byte[] imageBytes = decodeBase64Image(request.getProfileImageData());

            Path filePath = uploadPath.resolve(savedFileName);
            Files.write(filePath, imageBytes);

            return new ProfileImageUploadResponse(IMAGE_URL_PREFIX + savedFileName);
        } catch (IOException exception) {
            throw new IllegalArgumentException("internal_server_error");
        }
    }

    public void deleteProfileImage(String profileImageName) {
        try {
            Path filePath = Path.of(UPLOAD_DIRECTORY).resolve(profileImageName);

            if (!Files.exists(filePath)) {
                throw new IllegalArgumentException("image_not_found");
            }

            Files.delete(filePath);
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
        // 이미지 확장자 제한
        return extension.equals("png")
                || extension.equals("jpg")
                || extension.equals("jpeg")
                || extension.equals("webp");
    }

    private byte[] decodeBase64Image(String profileImageData) {
        // 실제 파일을 저장하려면 쉼표 뒤의 문자열만 디코딩
        String[] parts = profileImageData.split(",");

        if (parts.length != 2) {
            throw new IllegalArgumentException("invalid_request");
        }

        return Base64.getDecoder().decode(parts[1]);
    }
}