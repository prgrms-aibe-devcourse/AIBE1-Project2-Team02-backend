package aibe1.proj2.mentoss.feature.lecture.model.mapper;

import aibe1.proj2.mentoss.feature.lecture.model.dto.response.LectureCurriculumResponse;
import aibe1.proj2.mentoss.feature.lecture.model.dto.response.LectureDetailResponse;
import aibe1.proj2.mentoss.feature.lecture.model.dto.response.LectureResponse;
import aibe1.proj2.mentoss.feature.lecture.model.dto.response.LectureReviewResponse;
import aibe1.proj2.mentoss.global.entity.Lecture;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
            "#{price}, #{mentorId}, '[]', #{status})")
    void createLecture(Lecture lecture);

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
     * 강의 기본 정보 조회 - 생성자 매핑 사용
     */
    @Select("SELECT " +
            "l.lecture_id AS lectureId, " +
            "l.lecture_title AS lectureTitle, " +
            "u.nickname AS mentorNickname, " +
            "l.created_at AS createdAt, " +
            "l.updated_at AS updatedAt, " +
            "lc.parent_category AS parentCategory, " +
            "lc.middle_category AS middleCategory, " +
            "lc.subcategory AS subcategory, " +
            "l.is_closed AS isClosed, " +
            "l.status AS status " +
            "FROM lecture l " +
            "JOIN mentor_profile mp ON l.mentor_id = mp.mentor_id " +
            "JOIN app_user u ON mp.user_id = u.user_id " +
            "JOIN lecture_category lc ON l.category_id = lc.category_id " +
            "WHERE l.lecture_id = #{lectureId} " +
            "AND l.is_deleted = FALSE")
    @ConstructorArgs({
            @Arg(column = "lectureId", javaType = Long.class),
            @Arg(column = "lectureTitle", javaType = String.class),
            @Arg(column = "mentorNickname", javaType = String.class),
            @Arg(column = "createdAt", javaType = LocalDateTime.class),
            @Arg(column = "updatedAt", javaType = LocalDateTime.class),
            @Arg(column = "parentCategory", javaType = String.class),
            @Arg(column = "middleCategory", javaType = String.class),
            @Arg(column = "subcategory", javaType = String.class),
            @Arg(column = "isClosed", javaType = boolean.class),
            @Arg(column = "status", javaType = String.class)
    })
    LectureResponse getLectureById(@Param("lectureId") Long lectureId);

    /**
     * 강의 상세 정보 조회 - 생성자 매핑 사용
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
    @ConstructorArgs({
            @Arg(column = "lectureId", javaType = Long.class),
            @Arg(column = "lectureTitle", javaType = String.class),
            @Arg(column = "description", javaType = String.class),
            @Arg(column = "price", javaType = Long.class),
            @Arg(column = "timeSlots", javaType = String.class)
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
     * 강의 기본 정보 업데이트
     */
    @Update("UPDATE lecture SET " +
            "lecture_title = #{lectureTitle}, " +
            "description = #{description}, " +
            "category_id = #{categoryId}, " +
            "curriculum = #{curriculum}, " +
            "price = #{price}, " +
            "updated_at = CURRENT_TIMESTAMP " +
            "WHERE lecture_id = #{lectureId} " +
            "AND is_deleted = FALSE")
    int updateLecture(Lecture lecture);

    /**
     * 강의 지역 정보 삭제
     */
    @Delete("DELETE FROM lecture_region WHERE lecture_id = #{lectureId}")
    int deleteLectureRegions(@Param("lectureId") Long lectureId);

    /**
     * 강의가 존재하는지 확인
     */
    @Select("SELECT COUNT(*) FROM lecture WHERE lecture_id = #{lectureId} AND is_deleted = FALSE")
    int existsLectureById(@Param("lectureId") Long lectureId);



    /**
     * 강의 커리큘럼 조회 - 생성자 매핑 사용
     */
    @Select("SELECT " +
            "lecture_id AS lectureId, " +
            "curriculum " +
            "FROM lecture " +
            "WHERE lecture_id = #{lectureId} " +
            "AND is_deleted = FALSE")
    @ConstructorArgs({
            @Arg(column = "lectureId", javaType = Long.class),
            @Arg(column = "curriculum", javaType = String.class)
    })
    LectureCurriculumResponse getLectureCurriculum(@Param("lectureId") Long lectureId);

    /**
     * 강의 리뷰 목록 조회 - 생성자 매핑 사용
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
    @ConstructorArgs({
            @Arg(column = "reviewId", javaType = Long.class),
            @Arg(column = "lectureId", javaType = Long.class),
            @Arg(column = "writerNickname", javaType = String.class),
            @Arg(column = "content", javaType = String.class),
            @Arg(column = "rating", javaType = Double.class),
            @Arg(column = "createdAt", javaType = LocalDateTime.class)
    })
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


    /**
     * 강의 마감 오픈 상태 변경
     */
    @Update("UPDATE lecture SET is_closed = #{isClosed} WHERE lecture_id = #{lectureId} AND is_deleted = FALSE")
    int updateLectureClosed(@Param("lectureId") Long lectureId, @Param("isClosed") boolean isClosed);


}