package aibe1.proj2.mentoss.feature.lecture.model.mapper;

import aibe1.proj2.mentoss.feature.lecture.model.dto.request.LectureSearchRequest;
import aibe1.proj2.mentoss.feature.lecture.model.dto.response.*;
import aibe1.proj2.mentoss.global.entity.AppUser;
import aibe1.proj2.mentoss.global.entity.Lecture;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface LectureMapper {

    /**
     * 강의 생성
     */
    @Insert("INSERT INTO lecture (lecture_title, description, category_id, curriculum, "
            + "price, mentor_id, available_time_slots, status) "
            + "VALUES (#{lectureTitle}, #{description}, #{categoryId}, #{curriculum}, "
            + "#{price}, #{mentorId}, '[]', #{status})")
    void createLecture(Lecture lecture);

    /**
     * 강의 ID 조회 (생성 후)
     */
    @Select("SELECT LAST_INSERT_ID()")
    Long getLastInsertId();

    /**
     * 강의 지역 등록
     */
    @Insert("INSERT INTO lecture_region (lecture_id, region_code) VALUES (#{lectureId}, #{regionCode})")
    void insertLectureRegion(@Param("lectureId") Long lectureId, @Param("regionCode") String regionCode);

    /**
     * 강의 시간대 정보 업데이트 (JSON 형태로 저장)
     */
    @Update("UPDATE lecture SET available_time_slots = #{timeSlots} WHERE lecture_id = #{lectureId}")
    void updateLectureTimeSlots(@Param("lectureId") Long lectureId, @Param("timeSlots") String timeSlotsJson);

    /**
     * 강의 목록 조회 (필터링, 검색, 페이징)
     * 복잡한 쿼리는 @SelectProvider를 사용하여 동적 SQL을 생성할 수 있습니다.
     */
    @SelectProvider(type = LectureSqlProvider.class, method = "findLectures")
    List<LectureListResponse> findLectures(
            @Param("searchRequest") LectureSearchRequest searchRequest,
            @Param("pageSize") int pageSize,
            @Param("offset") int offset);

    /**
     * 강의 목록 총 개수 조회
     */
    @SelectProvider(type = LectureSqlProvider.class, method = "countLectures")
    long countLectures(@Param("searchRequest") LectureSearchRequest searchRequest);

    /**
     * 강의 기본 정보 조회
     */
    @Select("SELECT l.lecture_id AS lectureId, l.lecture_title AS lectureTitle, " +
            "u.nickname AS mentorNickname, l.created_at AS createdAt, " +
            "l.updated_at AS updatedAt, lc.parent_category AS parentCategory, " +
            "lc.middle_category AS middleCategory, lc.subcategory AS subcategory, " +
            "l.is_closed AS isClosed, l.status AS status, l.description AS description, " +
            "l.price AS price, l.curriculum AS curriculum, " +
            "(SELECT JSON_ARRAYAGG(CONCAT(r.sido, ' ', r.sigungu, ' ', IFNULL(r.dong, ''))) " +
            "FROM lecture_region lr JOIN region r ON lr.region_code = r.region_code " +
            "WHERE lr.lecture_id = l.lecture_id) AS regions, " +
            "l.available_time_slots AS timeSlots, " +
            "u.user_id AS authorUserId, " +
            "u.profile_image AS profileImage, " +
            "u.sex AS sex, " +
            "u.mbti AS mbti, " +
            "mp.education AS education, " +
            "mp.major AS major, " +
            "mp.is_certified AS isCertified, " +
            "mp.content AS content, " +
            "mp.appeal_file_url AS appealFileUrl, " +
            "mp.tag AS tag, " +
            "l.mentor_id AS mentorId " +
            "FROM lecture l " +
            "JOIN mentor_profile mp ON l.mentor_id = mp.mentor_id " +
            "JOIN app_user u ON mp.user_id = u.user_id " +
            "JOIN lecture_category lc ON l.category_id = lc.category_id " +
            "WHERE l.lecture_id = #{lectureId} AND l.is_deleted = FALSE")
    LectureResponse getLectureById(@Param("lectureId") Long lectureId);

    /**
     * 강의 지역 정보 조회
     */
    @Select("SELECT CONCAT(r.sido, ' ', r.sigungu, ' ', IFNULL(r.dong, '')) " +
            "FROM lecture_region lr " +
            "JOIN region r ON lr.region_code = r.region_code " +
            "WHERE lr.lecture_id = #{lectureId}")
    List<String> getLectureRegions(@Param("lectureId") Long lectureId);

    /**
     * 강의 기본 정보 업데이트
     */
    @Update("UPDATE lecture SET lecture_title = #{lectureTitle}, " +
            "description = #{description}, category_id = #{categoryId}, " +
            "curriculum = #{curriculum}, price = #{price}, " +
            "updated_at = CURRENT_TIMESTAMP " +
            "WHERE lecture_id = #{lectureId} AND is_deleted = FALSE")
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
     * 강의 삭제 (soft delete)
     */
    @Update("UPDATE lecture SET is_deleted = TRUE, deleted_at = CURRENT_TIMESTAMP " +
            "WHERE lecture_id = #{lectureId} AND is_deleted = FALSE")
    int softDeleteLecture(@Param("lectureId") Long lectureId);

    /**
     * 강의 커리큘럼 조회
     */
    @Select("SELECT lecture_id AS lectureId, curriculum " +
            "FROM lecture " +
            "WHERE lecture_id = #{lectureId} AND is_deleted = FALSE")
    LectureCurriculumResponse getLectureCurriculum(@Param("lectureId") Long lectureId);

    /**
     * 강의 리뷰 목록 조회
     */
    @Select("SELECT r.review_id AS reviewId, r.lecture_id AS lectureId, " +
            "u.nickname AS writerNickname, r.content, r.rating, " +
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

    /**
     * 강의 마감 오픈 상태 변경
     */
    @Update("UPDATE lecture SET is_closed = #{isClosed} " +
            "WHERE lecture_id = #{lectureId} " +
            "AND is_deleted = FALSE")
    int updateLectureClosed(@Param("lectureId") Long lectureId, @Param("isClosed") boolean isClosed);

    /**
     * 사용자 아이디 조회
     */
    @Select("SELECT * FROM app_user WHERE user_id = #{userId}")
    AppUser findUserById(@Param("userId") Long userId);

    @Select("SELECT COUNT(*) > 0 FROM lecture l " +
            "JOIN mentor_profile mp ON l.mentor_id = mp.mentor_id " +
            "WHERE l.lecture_id = #{lectureId} AND mp.user_id = #{userId}")
    boolean checkLectureOwner(@Param("lectureId") Long lectureId, @Param("userId") Long userId);

    /**
     * 사용자가 멘토 프로필을 가지고 있는지 확인
     */
    @Select("SELECT COUNT(*) > 0 FROM mentor_profile WHERE user_id = #{userId}")
    boolean checkMentorProfileByUserId(@Param("userId") Long userId);

    /**
     * 사용자의 멘토 ID 조회
     */
    @Select("SELECT mentor_id FROM mentor_profile WHERE user_id = #{userId}")
    Long getMentorIdByUserId(@Param("userId") Long userId);

    /**
     *
     * 회원 삭제 시 멘토의 모든 강의 소프트 삭제
     */
    @Update("UPDATE lecture l " +
            "JOIN mentor_profile mp ON l.mentor_id = mp.mentor_id " +
            "SET l.is_deleted = TRUE, l.deleted_at = CURRENT_TIMESTAMP " +
            "WHERE mp.user_id = #{userId}")
    int softDeleteLecturesByMentorId(@Param("userId") Long userId);


    /**
     * 특정 멘토의 모집 중인 모든 강의 마감 처리
     */
    @Update("UPDATE lecture SET " +
            "is_closed = TRUE, " +
            "updated_at = CURRENT_TIMESTAMP " +
            "WHERE mentor_id = #{mentorId} " +
            "AND is_closed = FALSE " +
            "AND is_deleted = FALSE")
    int closeAllOpenLecturesByMentorId(Long mentorId);
}