package aibe1.proj2.mentoss.global.dto;

/**
 * 공통 API 응답 포맷 클래스
 *
 * @param <T> 응답 데이터 타입
 *
 * 이 클래스는 모든 API 응답을 일관된 구조로 제공하기 위해 사용됩니다.
 * - success: 요청 성공 여부
 * - message: 응답 메시지 (성공/실패 이유)
 * - data: 실제 반환 데이터 (제네릭 타입)
 */
public record ApiResponseFormat<T>(
        boolean success,   // 요청 성공 여부 (true/false)
        String message,    // 응답 메시지 (ex: 성공했습니다, 실패 사유 등)
        T data             // 응답 데이터 (없을 경우 null)
) {

    /**
     * 성공한 요청에 대한 응답을 생성하는 정적 메서드
     *
     * @param data 응답에 담을 실제 데이터
     * @return 성공 상태의 ApiResponseFormat 객체
     */
    public static <T> ApiResponseFormat<T> ok(T data) {
        return new ApiResponseFormat<>(true, "요청이 성공적으로 처리되었습니다.", data);
    }

    /**
     * 실패한 요청에 대한 응답을 생성하는 정적 메서드
     *
     * @param message 실패 원인을 설명하는 메시지
     * @return 실패 상태의 ApiResponseFormat 객체
     */
    public static <T> ApiResponseFormat<T> fail(String message) {
        return new ApiResponseFormat<>(false, message, null);
    }
}