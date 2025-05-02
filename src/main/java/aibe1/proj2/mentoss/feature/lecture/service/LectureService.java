package aibe1.proj2.mentoss.feature.lecture.service;

import aibe1.proj2.mentoss.feature.lecture.model.dto.request.LectureCreateRequest;
import aibe1.proj2.mentoss.feature.lecture.model.dto.request.LectureUpdateRequest;
import aibe1.proj2.mentoss.feature.lecture.model.dto.response.LectureCurriculumResponse;
import aibe1.proj2.mentoss.feature.lecture.model.dto.response.LectureDetailResponse;
import aibe1.proj2.mentoss.feature.lecture.model.dto.response.LectureResponse;
import aibe1.proj2.mentoss.feature.lecture.model.dto.response.LectureReviewsResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 강의 서비스 인터페이스
 */
public interface LectureService {

    /**
     * 강의 생성
     */
    Long createLecture(LectureCreateRequest request) throws JsonProcessingException;

    /**
     * 강의 기본 정보 조회
     */
    LectureResponse getLecture(Long lectureId);

    /**
     * 강의 상세 정보 조회
     */
    LectureDetailResponse getLectureDetail(Long lectureId) throws JsonProcessingException;


    /**
     * 강의 정보 수정
     * @return 수정 성공 여부
     */
    boolean updateLecture(Long lectureId, LectureUpdateRequest request) throws JsonProcessingException;

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

}