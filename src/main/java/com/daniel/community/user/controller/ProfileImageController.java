package com.daniel.community.user.controller;

import com.daniel.community.global.response.ApiResponse;
import com.daniel.community.user.dto.ProfileImageUploadRequest;
import com.daniel.community.user.dto.ProfileImageUploadResponse;
import com.daniel.community.user.service.ProfileImageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileImageController {

    private final ProfileImageService profileImageService;

    public ProfileImageController(ProfileImageService profileImageService) {
        this.profileImageService = profileImageService;
    }

    @PostMapping("/users/profile-images")
    public ResponseEntity<ApiResponse> uploadProfileImage(
            @Valid @RequestBody ProfileImageUploadRequest request
    ) {
        try {
            ProfileImageUploadResponse response =
                    profileImageService.uploadProfileImage(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success("upload_success", response));
        } catch (IllegalArgumentException exception) {
            HttpStatus status = getProfileImageErrorStatus(exception.getMessage());

            return ResponseEntity
                    .status(status)
                    .body(ApiResponse.error(exception.getMessage()));
        }
    }

    @DeleteMapping("/users/profile-images/{profileImageName}")
    public ResponseEntity<ApiResponse> deleteProfileImage(
            @PathVariable String profileImageName
    ) {
        try {
            profileImageService.deleteProfileImage(profileImageName);

            return ResponseEntity.ok(ApiResponse.success("delete_success"));
        } catch (IllegalArgumentException exception) {
            HttpStatus status = getProfileImageErrorStatus(exception.getMessage());

            return ResponseEntity
                    .status(status)
                    .body(ApiResponse.error(exception.getMessage()));
        }
    }

    private HttpStatus getProfileImageErrorStatus(String message) {
        if ("image_not_found".equals(message)) {
            return HttpStatus.NOT_FOUND;
        }

        if ("internal_server_error".equals(message)) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return HttpStatus.BAD_REQUEST;
    }
}