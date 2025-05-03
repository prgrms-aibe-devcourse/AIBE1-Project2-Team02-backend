package aibe1.proj2.mentoss.feature.lecture.model.mapper;

import aibe1.proj2.mentoss.feature.lecture.model.dto.request.LectureSearchRequest;
import aibe1.proj2.mentoss.feature.lecture.model.dto.response.*;
import aibe1.proj2.mentoss.global.entity.Lecture;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface LectureMapper {

    /**
     * 강의 생성
     */
    void createLecture(Lecture lecture);

    /**
     * 강의 ID 조회 (생성 후)
     */
    Long getLastInsertId( );

    /**
     * 강의 지역 등록
     */
    void insertLectureRegion(@Param("lectureId") Long lectureId, @Param("regionCode") String regionCode);

    /**
     * 강의 시간대 정보 업데이트 (JSON 형태로 저장)
     */
    void updateLectureTimeSlots(@Param("lectureId") Long lectureId, @Param("timeSlots") String timeSlotsJson);

    /**
     * 강의 목록 조회 (필터링, 검색, 페이징)
     */
    List<LectureListResponse> findLectures(
            @Param("searchRequest") LectureSearchRequest searchRequest,
            @Param("pageSize") int pageSize,
            @Param("offset") int offset);

    /**
     * 강의 목록 총 개수 조회
     */
    long countLectures(@Param("searchRequest") LectureSearchRequest searchRequest);

    /**
     * 강의 기본 정보 조회
     */
    LectureResponse getLectureById(@Param("lectureId") Long lectureId);


    /**
     * 강의 지역 정보 조회
     */
    List<String> getLectureRegions(@Param("lectureId") Long lectureId);

    /**
     * 강의 기본 정보 업데이트
     */
    int updateLecture(Lecture lecture);

    /**
     * 강의 지역 정보 삭제
     */
    int deleteLectureRegions(@Param("lectureId") Long lectureId);

    /**
     * 강의가 존재하는지 확인
     */
    int existsLectureById(@Param("lectureId") Long lectureId);

    /**
     * 강의 삭제 (soft delete)
     */
    int softDeleteLecture(@Param("lectureId") Long lectureId);

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

    /**
     * 강의 마감 오픈 상태 변경
     */
    int updateLectureClosed(@Param("lectureId") Long lectureId, @Param("isClosed") boolean isClosed);
}