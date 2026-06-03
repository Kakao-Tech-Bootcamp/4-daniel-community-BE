package com.daniel.community.user.controller;

import com.daniel.community.global.response.ApiResponse;
import com.daniel.community.global.security.CustomUserDetails;
import com.daniel.community.user.dto.SignupRequest;
import com.daniel.community.user.dto.SignupResponse;
import com.daniel.community.user.dto.UpdatePasswordRequest;
import com.daniel.community.user.dto.UpdateUserRequest;
import com.daniel.community.user.dto.UserInfoResponse;
import com.daniel.community.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@Valid @RequestBody SignupRequest request) {
        try {
            SignupResponse response = userService.signup(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success("register_success", response));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(exception.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        try {
            UserInfoResponse response = userService.getMyInfo(userDetails.getUserId());

            return ResponseEntity.ok(
                    ApiResponse.success("get_user_info_success", response)
            );
        } catch (IllegalArgumentException exception) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(exception.getMessage()));
        }
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse> updateMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateUserRequest request
    ) {
        try {
            userService.updateMyInfo(userDetails.getUserId(), request);

            return ResponseEntity.ok(ApiResponse.success("update_success"));
        } catch (IllegalArgumentException exception) {
            HttpStatus status = getUpdateStatus(exception.getMessage());

            return ResponseEntity
                    .status(status)
                    .body(ApiResponse.error(exception.getMessage()));
        }
    }

    @PatchMapping("/me/password")
    public ResponseEntity<ApiResponse> updatePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdatePasswordRequest request
    ) {
        try {
            userService.updatePassword(userDetails.getUserId(), request);

            return ResponseEntity.ok(ApiResponse.success("password_update_success"));
        } catch (IllegalArgumentException exception) {
            HttpStatus status = getPasswordUpdateStatus(exception.getMessage());

            return ResponseEntity
                    .status(status)
                    .body(ApiResponse.error(exception.getMessage()));
        }
    }

    @GetMapping("/emails/{email}")
    public ResponseEntity<ApiResponse> checkEmail(@PathVariable String email) {
        try {
            userService.checkEmail(email);
            return ResponseEntity.ok(ApiResponse.success("email_available"));
        } catch (IllegalArgumentException exception) {
            HttpStatus status = getCheckStatus(exception.getMessage());

            return ResponseEntity
                    .status(status)
                    .body(ApiResponse.error(exception.getMessage()));
        }
    }

    @GetMapping("/nicknames/{nickname}")
    public ResponseEntity<ApiResponse> checkNickname(@PathVariable String nickname) {
        try {
            userService.checkNickname(nickname);
            return ResponseEntity.ok(ApiResponse.success("nickname_available"));
        } catch (IllegalArgumentException exception) {
            HttpStatus status = getCheckStatus(exception.getMessage());

            return ResponseEntity
                    .status(status)
                    .body(ApiResponse.error(exception.getMessage()));
        }
    }

    private HttpStatus getCheckStatus(String message) {
        if (message.endsWith("_duplicated")) {
            return HttpStatus.CONFLICT;
        }

        return HttpStatus.BAD_REQUEST;
    }

    private HttpStatus getUpdateStatus(String message) {
        if ("unauthorized".equals(message)) {
            return HttpStatus.UNAUTHORIZED;
        }

        if ("nickname_duplicated".equals(message)) {
            return HttpStatus.CONFLICT;
        }

        return HttpStatus.BAD_REQUEST;
    }

    private HttpStatus getPasswordUpdateStatus(String message) {
        if ("unauthorized".equals(message)) {
            return HttpStatus.UNAUTHORIZED;
        }

        return HttpStatus.BAD_REQUEST;
    }
}