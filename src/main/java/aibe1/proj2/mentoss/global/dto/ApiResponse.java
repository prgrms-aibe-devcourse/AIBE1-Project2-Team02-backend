package aibe1.proj2.mentoss.global.dto;

public record ApiResponse<T>(
boolean success,
String message,
T data
) {
public static <T> ApiResponse<T> ok(T data) {
    return new ApiResponse<>(true, "요청이 성공적으로 처리되었습니다.", data);
}

public static <T> ApiResponse<T> fail(String message) {
    return new ApiResponse<>(false, message, null);
}
}