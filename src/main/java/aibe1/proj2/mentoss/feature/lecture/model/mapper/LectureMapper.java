package aibe1.proj2.mentoss.feature.lecture.model.mapper;

import aibe1.proj2.mentoss.feature.lecture.model.dto.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface LectureMapper {

    /**
     * 강의 생성
     */
    @Insert("INSERT INTO lecture (" +
            "lecture_title, description, category_id, curriculum, " +
            "price, mentor_id, available_time_slots, status) " +
            "VALUES (" +
            "#{lectureTitle}, #{description}, #{categoryId}, #{curriculum}, " +
            "#{price}, 1, '[]', 'AVAILABLE')")
    void createLecture(LectureCreateRequest request);

    /**
     * 강의 ID 조회 (생성 후)
     */
    @Select("SELECT LAST_INSERT_ID()")
    Long getLastInsertId();

    /**
     * 강의 지역 등록
     */
    @Insert("INSERT INTO lecture_region (lecture_id, region_code) " +
            "VALUES (#{lectureId}, #{regionCode})")
    void insertLectureRegion(@Param("lectureId") Long lectureId, @Param("regionCode") String regionCode);

    /**
     * 강의 시간대 정보 업데이트 (JSON 형태로 저장)
     */
    @Update("UPDATE lecture SET available_time_slots = #{timeSlots} " +
            "WHERE lecture_id = #{lectureId}")
    void updateLectureTimeSlots(@Param("lectureId") Long lectureId, @Param("timeSlots") String timeSlotsJson);

    /**
     * 강의 기본 정보 조회 - 컬럼 별칭 명시적으로 지정
     */
    @Select("SELECT " +
            "l.lecture_id AS lectureId, " +
            "l.lecture_title AS lectureTitle, " +
            "u.nickname AS mentorNickname, " +
            "l.created_at AS createdAt, " +
            "lc.parent_category AS parentCategory, " +
            "lc.middle_category AS middleCategory, " +
            "lc.subcategory AS subcategory, " +
            "l.is_closed AS isClosed, " +
            "l.status AS status " +  // 별칭 추가
            "FROM lecture l " +
            "JOIN mentor_profile mp ON l.mentor_id = mp.mentor_id " +
            "JOIN app_user u ON mp.user_id = u.user_id " +
            "JOIN lecture_category lc ON l.category_id = lc.category_id " +
            "WHERE l.lecture_id = #{lectureId} " +
            "AND l.is_deleted = FALSE")
    @Results({
            @Result(property = "lectureId", column = "lectureId"),
            @Result(property = "lectureTitle", column = "lectureTitle"),
            @Result(property = "mentorNickname", column = "mentorNickname"),
            @Result(property = "createdAt", column = "createdAt"),
            @Result(property = "parentCategory", column = "parentCategory"),
            @Result(property = "middleCategory", column = "middleCategory"),
            @Result(property = "subcategory", column = "subcategory"),
            @Result(property = "isClosed", column = "isClosed"),
            @Result(property = "status", column = "status")
    })
    LectureResponse getLectureById(@Param("lectureId") Long lectureId);

    /**
     * 강의 상세 정보 조회 - 명시적 컬럼 매핑 추가
     */
    @Select("SELECT " +
            "l.lecture_id AS lectureId, " +
            "l.lecture_title AS lectureTitle, " +
            "l.description, " +
            "l.price, " +
            "l.available_time_slots AS timeSlots " +
            "FROM lecture l " +
            "WHERE l.lecture_id = #{lectureId} " +
            "AND l.is_deleted = FALSE")
    @Results({
            @Result(property = "lectureId", column = "lectureId"),
            @Result(property = "lectureTitle", column = "lectureTitle"),
            @Result(property = "description", column = "description"),
            @Result(property = "price", column = "price"),
            @Result(property = "timeSlots", column = "timeSlots")
    })
    LectureDetailResponse getLectureDetailById(@Param("lectureId") Long lectureId);

    /**
     * 강의 지역 정보 조회
     */
    @Select("SELECT " +
            "CONCAT(r.sido, ' ', r.sigungu, ' ', IFNULL(r.dong, '')) " +
            "FROM lecture_region lr " +
            "JOIN region r ON lr.region_code = r.region_code " +
            "WHERE lr.lecture_id = #{lectureId}")
    List<String> getLectureRegions(@Param("lectureId") Long lectureId);

    /**
     * 강의 커리큘럼 조회
     */
    @Select("SELECT " +
            "lecture_id AS lectureId, " +
            "curriculum " +
            "FROM lecture " +
            "WHERE lecture_id = #{lectureId} " +
            "AND is_deleted = FALSE")
    LectureCurriculumResponse getLectureCurriculum(@Param("lectureId") Long lectureId);

    /**
     * 강의 리뷰 목록 조회
     */
    @Select("SELECT " +
            "r.review_id AS reviewId, " +
            "r.lecture_id AS lectureId, " +
            "u.nickname AS writerNickname, " +
            "r.content, " +
            "r.rating, " +
            "r.created_at AS createdAt " +
            "FROM review r " +
            "JOIN app_user u ON r.writer_id = u.user_id " +
            "WHERE r.lecture_id = #{lectureId} " +
            "AND r.is_deleted = FALSE " +
            "AND r.status = 'AVAILABLE' " +
            "ORDER BY r.created_at DESC")
    List<LectureReviewResponse> getLectureReviews(@Param("lectureId") Long lectureId);

    /**
     * 강의 평균 평점 조회
     */
    @Select("SELECT COALESCE(AVG(rating), 0) " +
            "FROM review " +
            "WHERE lecture_id = #{lectureId} " +
            "AND is_deleted = FALSE " +
            "AND status = 'AVAILABLE'")
    Double getLectureAverageRating(@Param("lectureId") Long lectureId);

    /**
     * 강의 리뷰 개수 조회
     */
    @Select("SELECT COUNT(*) " +
            "FROM review " +
            "WHERE lecture_id = #{lectureId} " +
            "AND is_deleted = FALSE " +
            "AND status = 'AVAILABLE'")
    Long getLectureReviewCount(@Param("lectureId") Long lectureId);
}