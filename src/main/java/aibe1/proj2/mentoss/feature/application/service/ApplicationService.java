package aibe1.proj2.mentoss.feature.application.service;

import aibe1.proj2.mentoss.feature.application.model.dto.*;

import java.util.List;

public interface ApplicationService {

    List<AppliedLectureResponseDto> getMyAppliedLectures(Long menteeId);

    List<LectureResponseDto> getLecturesByMentor(Long mentorId);

    List<LectureApplicantDto> getApplicantsByLectureId(Long lectureId);

    void approveApplication(Long applicationId, Long senderId);

    void rejectApplication(Long applicationId, String rejectReason , Long senderId);

    LectureApplyFormDto getLectureApplyForm(Long lectureId);

    void applyForLecture(LectureApplyRequestDto dto, Long menteeId);
}
