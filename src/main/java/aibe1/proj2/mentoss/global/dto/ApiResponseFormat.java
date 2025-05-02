package aibe1.proj2.mentoss.global.dto;

public record ApiResponseFormat<T>(
boolean success,
String message,
T data
) {
public static <T> ApiResponseFormat<T> ok(T data) {
    return new ApiResponseFormat<>(true, "요청이 성공적으로 처리되었습니다.", data);
}

public static <T> ApiResponseFormat<T> fail(String message) {
    return new ApiResponseFormat<>(false, message, null);
}
}