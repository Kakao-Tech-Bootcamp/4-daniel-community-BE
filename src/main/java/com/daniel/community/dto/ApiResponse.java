package com.daniel.community.dto;

public class ApiResponse {

    private String message;

    // 서로 다른 응답 DTO를 담기 위해 Object 사용
    private Object data;

    public ApiResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public static ApiResponse success(String message, Object data) {
        return new ApiResponse(message, data);
    }

    public static ApiResponse success(String message) {
        return new ApiResponse(message, null);
    }

    public static ApiResponse error(String message) {
        return new ApiResponse(message, null);
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
