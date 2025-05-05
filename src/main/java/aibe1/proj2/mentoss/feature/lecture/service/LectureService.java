package aibe1.proj2.mentoss.feature.lecture.service;

import aibe1.proj2.mentoss.feature.lecture.model.dto.request.LectureCreateRequest;
import aibe1.proj2.mentoss.feature.lecture.model.dto.request.LectureSearchRequest;
import aibe1.proj2.mentoss.feature.lecture.model.dto.request.LectureUpdateRequest;
import aibe1.proj2.mentoss.feature.lecture.model.dto.response.*;
import aibe1.proj2.mentoss.global.entity.AppUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 강의 서비스 인터페이스
 */
public interface LectureService {

    /**
     * 강의 생성
     */
    Long createLecture(LectureCreateRequest request, Long userId) throws JsonProcessingException;


    /**
     * 강의 목록 조회 (필터링, 검색, 페이징)
     */
    Page<LectureListResponse> getLectures(LectureSearchRequest searchRequest, Pageable pageable);

    /**
     * 강의 기본 정보 조회
     */
    LectureResponse getLecture(Long lectureId);


    /**
     * 강의 정보 수정
     * @return 수정 성공 여부
     */
    boolean updateLecture(Long lectureId, LectureUpdateRequest request) throws JsonProcessingException;


    /**
     * 강의 삭제 (soft delete)
     * @param lectureId 삭제할 강의 ID
     * @return 삭제 성공 여부
     */
    boolean deleteLecture(Long lectureId);

    /**
     * 강의 커리큘럼 조회
     */
    LectureCurriculumResponse getLectureCurriculum(Long lectureId);

    /**
     * 강의 리뷰 조회
     */
    LectureReviewsResponse getLectureReviews(Long lectureId);


    /**
     * 강의 마감 상태 변경
     * @param lectureId 강의 ID
     * @param isClosed true: 마감, false: 오픈
     * @return 변경 성공 여부
     */
    boolean updateLectureClosed(Long lectureId, boolean isClosed);

    /**
     * 아이디 조회
     */
    AppUser getUserById(Long userId);

    /**
     * 게시글 주인 확인
     */
    boolean isLectureOwner(Long lectureId, Long userId);


}