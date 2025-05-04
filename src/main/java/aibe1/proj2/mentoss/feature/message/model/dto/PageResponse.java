package aibe1.proj2.mentoss.feature.message.model.dto;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        int totalPages,
        long totalCount
) {
    public static <T> PageResponse<T> of(List<T> content, int page, int size, long totalCount) {
        int totalPages = (int) Math.ceil((double) totalCount / size);
        return new PageResponse<>(content, page, size, totalPages, totalCount);
    }
}