package aibe1.proj2.mentoss.feature.lecture.service;

import aibe1.proj2.mentoss.feature.lecture.model.dto.request.LectureCreateRequest;
import aibe1.proj2.mentoss.feature.lecture.model.dto.request.LectureRegionRequest;
import aibe1.proj2.mentoss.feature.lecture.model.dto.response.*;
import aibe1.proj2.mentoss.feature.lecture.model.entity.Lecture;
import aibe1.proj2.mentoss.feature.lecture.model.mapper.LectureMapper;
import aibe1.proj2.mentoss.global.exception.EntityNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
     * 강의 상세 정보 조회
     */
    @Override
    public LectureDetailResponse getLectureDetail(Long lectureId) throws JsonProcessingException {
        // 기본 정보 조회
        LectureDetailResponse lectureDetail = lectureMapper.getLectureDetailById(lectureId);
        if (lectureDetail == null) {
            throw new EntityNotFoundException("해당 강의를 찾을 수 없습니다. (ID: " + lectureId + ")");
        }

        // 지역 정보 조회
        List<String> regions = lectureMapper.getLectureRegions(lectureId);

        // 새 Record 인스턴스 생성 (이미 regions가 포함된 생성자 호출)
        return new LectureDetailResponse(
                lectureDetail.lectureId(),
                lectureDetail.lectureTitle(),
                lectureDetail.description(),
                lectureDetail.price(),
                lectureDetail.timeSlots(),
                regions
        );
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
}