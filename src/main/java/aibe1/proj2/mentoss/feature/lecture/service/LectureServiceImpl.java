package aibe1.proj2.mentoss.feature.lecture.service;

import aibe1.proj2.mentoss.feature.lecture.model.dto.request.LectureCreateRequest;
import aibe1.proj2.mentoss.feature.lecture.model.dto.request.LectureRegionRequest;
import aibe1.proj2.mentoss.feature.lecture.model.dto.request.LectureSearchRequest;
import aibe1.proj2.mentoss.feature.lecture.model.dto.request.LectureUpdateRequest;
import aibe1.proj2.mentoss.feature.lecture.model.dto.response.*;
import aibe1.proj2.mentoss.global.entity.Lecture;
import aibe1.proj2.mentoss.feature.lecture.model.mapper.LectureMapper;
import aibe1.proj2.mentoss.global.entity.Lecture;
import aibe1.proj2.mentoss.global.exception.EntityNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 강의 서비스 구현체
 */
@Service
public class LectureServiceImpl implements LectureService {

    private final LectureMapper lectureMapper;
    private final ObjectMapper objectMapper;

    public LectureServiceImpl(LectureMapper lectureMapper, ObjectMapper objectMapper) {
        this.lectureMapper = lectureMapper;
        this.objectMapper = objectMapper;
    }

    /**
     * 강의 생성
     */
    @Override
    @Transactional
    public Long createLecture(LectureCreateRequest request) throws JsonProcessingException {
        // DTO → 엔티티 변환
        Lecture lecture = new Lecture();
        lecture.setLectureTitle(request.lectureTitle());
        lecture.setDescription(request.description());
        lecture.setCategoryId(request.categoryId());
        lecture.setCurriculum(request.curriculum());
        lecture.setPrice(request.price());
        lecture.setMentorId(1L); // 임시 값
        lecture.setStatus("AVAILABLE");

        // 엔티티 저장 로직
        lectureMapper.createLecture(lecture);
        Long lectureId = lectureMapper.getLastInsertId();

        // 지역 정보 저장
        for (LectureRegionRequest region : request.regions()) {
            lectureMapper.insertLectureRegion(lectureId, region.regionCode());
        }

        // 시간 정보 저장
        String timeSlotsJson = objectMapper.writeValueAsString(request.timeSlots());
        lecture.setAvailableTimeSlots(timeSlotsJson);
        lectureMapper.updateLectureTimeSlots(lectureId, timeSlotsJson);

        return lectureId;
    }

    @Override
    public Page<LectureListResponse> getLectures(LectureSearchRequest searchRequest, Pageable pageable) {
        // 페이징 정보 추출
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int offset = pageNumber * pageSize; // 오프셋 계산

        // 총 개수 조회
        long totalCount = lectureMapper.countLectures(searchRequest);

        // 강의 목록 조회
        List<LectureListResponse> lectures = lectureMapper.findLectures(
                searchRequest, pageSize, offset); // 계산된 오프셋 전달

        // Page 객체 생성 및 반환 (스프링 데이터 활용)
        return new PageImpl<>(lectures, pageable, totalCount);
    }


    /**
     * 강의 기본 정보 조회
     */
    @Override
    public LectureResponse getLecture(Long lectureId) {
        LectureResponse lecture = lectureMapper.getLectureById(lectureId);
        if (lecture == null) {
            throw new EntityNotFoundException("해당 강의를 찾을 수 없습니다. (ID: " + lectureId + ")");
        }
        return lecture;
    }


    /**
     * 강의 정보 수정
     */
    @Override
    @Transactional
    public boolean updateLecture(Long lectureId, LectureUpdateRequest request) throws JsonProcessingException {
        // 강의 존재 여부 확인
        int exists = lectureMapper.existsLectureById(lectureId);
        if (exists == 0) {
            throw new EntityNotFoundException("해당 강의를 찾을 수 없습니다. (ID: " + lectureId + ")");
        }

        // DTO → 엔티티 변환
        Lecture lecture = new Lecture();
        lecture.setLectureId(lectureId);
        lecture.setLectureTitle(request.lectureTitle());
        lecture.setDescription(request.description());
        lecture.setCategoryId(request.categoryId());
        lecture.setCurriculum(request.curriculum());
        lecture.setPrice(request.price());

        // 기본 정보 업데이트
        int updatedRows = lectureMapper.updateLecture(lecture);
        if (updatedRows == 0) {
            return false;
        }

        // 시간 정보 업데이트
        String timeSlotsJson = objectMapper.writeValueAsString(request.timeSlots());
        lectureMapper.updateLectureTimeSlots(lectureId, timeSlotsJson);

        // 지역 정보 업데이트 (기존 지역 정보 삭제 후 새로 추가)
        lectureMapper.deleteLectureRegions(lectureId);
        for (LectureRegionRequest region : request.regions()) {
            lectureMapper.insertLectureRegion(lectureId, region.regionCode());
        }

        return true;
    }

    /**
     * 강의 삭제 (soft delete)
     */
    @Override
    @Transactional
    public boolean deleteLecture(Long lectureId) {
        // 강의 존재 여부 확인
        int exists = lectureMapper.existsLectureById(lectureId);
        if (exists == 0) {
            throw new EntityNotFoundException("해당 강의를 찾을 수 없습니다. (ID: " + lectureId + ")");
        }

        // 강의 삭제 (soft delete)
        int deletedRows = lectureMapper.softDeleteLecture(lectureId);
        return deletedRows > 0;
    }

    /**
     * 강의 커리큘럼 조회
     */
    @Override
    public LectureCurriculumResponse getLectureCurriculum(Long lectureId) {
        LectureCurriculumResponse curriculum = lectureMapper.getLectureCurriculum(lectureId);
        if (curriculum == null) {
            throw new EntityNotFoundException("해당 강의를 찾을 수 없습니다. (ID: " + lectureId + ")");
        }
        return curriculum;
    }

    /**
     * 강의 리뷰 조회
     */
    @Override
    public LectureReviewsResponse getLectureReviews(Long lectureId) {
        // 강의 존재 확인
        if (lectureMapper.getLectureById(lectureId) == null) {
            throw new EntityNotFoundException("해당 강의를 찾을 수 없습니다. (ID: " + lectureId + ")");
        }

        List<LectureReviewResponse> reviews = lectureMapper.getLectureReviews(lectureId);
        Double averageRating = lectureMapper.getLectureAverageRating(lectureId);
        Long reviewCount = lectureMapper.getLectureReviewCount(lectureId);

        return new LectureReviewsResponse(reviews, averageRating, reviewCount);
    }


    /**
     * 강의 오픈/마감으로 변경
     */
    @Override
    @Transactional
    public boolean updateLectureClosed(Long lectureId, boolean isClosed) {
        // 강의 존재 여부 확인
        LectureResponse lecture = lectureMapper.getLectureById(lectureId);
        if (lecture == null) {
            throw new EntityNotFoundException("해당 강의를 찾을 수 없습니다. (ID: " + lectureId + ")");
        }

        // 이미 동일한 상태인지 확인
        if (lecture.isClosed() == isClosed) {
            return false; // 이미 동일한 상태인 경우
        }

        // 강의 오픈/마감 상태 변경
        int updatedRows = lectureMapper.updateLectureClosed(lectureId, isClosed);
        return updatedRows > 0;
    }


}