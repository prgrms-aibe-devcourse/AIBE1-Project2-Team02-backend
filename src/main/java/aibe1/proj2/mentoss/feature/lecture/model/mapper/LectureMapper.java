package aibe1.proj2.mentoss.feature.lecture.model.mapper;

import aibe1.proj2.mentoss.feature.lecture.model.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LectureMapper {
    /**
     * 강의 생성
     */
    void createLecture(LectureCreateRequest request);

    /**
     * 강의 ID 조회 (생성 후)
     */
    Long getLastInsertId();

    /**
     * 강의 지역 등록
     */
    void insertLectureRegion(@Param("lectureId") Long lectureId, @Param("regionCode") String regionCode);

    /**
     * 강의 시간대 정보 등록 (JSON 형태로 저장)
     */
    void updateLectureTimeSlots(@Param("lectureId") Long lectureId, @Param("timeSlots") String timeSlotsJson);

    /**
     * 강의 기본 정보 조회
     */
    LectureResponse getLectureById(@Param("lectureId") Long lectureId);

    /**
     * 강의 상세 정보 조회
     */
    LectureDetailResponse getLectureDetailById(@Param("lectureId") Long lectureId);

    /**
     * 강의 지역 정보 조회
     */
    List<String> getLectureRegions(@Param("lectureId") Long lectureId);

    /**
     * 강의 커리큘럼 조회
     */
    LectureCurriculumResponse getLectureCurriculum(@Param("lectureId") Long lectureId);

    /**
     * 강의 리뷰 목록 조회
     */
    List<LectureReviewResponse> getLectureReviews(@Param("lectureId") Long lectureId);

    /**
     * 강의 평균 평점 조회
     */
    Double getLectureAverageRating(@Param("lectureId") Long lectureId);

    /**
     * 강의 리뷰 개수 조회
     */
    Long getLectureReviewCount(@Param("lectureId") Long lectureId);
}
