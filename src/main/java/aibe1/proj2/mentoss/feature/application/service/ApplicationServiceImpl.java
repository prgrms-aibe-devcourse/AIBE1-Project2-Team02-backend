package aibe1.proj2.mentoss.feature.application.service;

import aibe1.proj2.mentoss.feature.application.model.dto.*;
import aibe1.proj2.mentoss.feature.application.model.mapper.ApplicationMapper;
import aibe1.proj2.mentoss.feature.message.model.dto.MessageSendRequestDto;
import aibe1.proj2.mentoss.feature.message.service.MessageService;
import aibe1.proj2.mentoss.global.entity.enums.ApplicationStatus;
import aibe1.proj2.mentoss.global.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationMapper applicationMapper;
    private final MessageService messageService;
    private final ObjectMapper objectMapper;

    @Override
    public List<AppliedLectureResponseDto> getMyAppliedLectures(Long menteeId) {
        return applicationMapper.findMyAppliedLectures(menteeId);
    }

    @Override
    public List<LectureResponseDto> getLecturesByMentor(Long mentorId) {
        return applicationMapper.findLecturesByMentorId(mentorId);
    }

    @Override
    public List<LectureApplicantDto> getApplicantsByLectureId(Long lectureId) {
        return applicationMapper.findApplicantsByLectureId(lectureId);
    }

    @Transactional
    @Override
    public void approveApplication(Long applicationId, Long senderId) {
        ApplicationInfoDto info = validatePendingApplication(applicationId);
        applicationMapper.acceptApplication(applicationId);

        String content = String.format("'%s' 과외 신청이 수락되었습니다.", info.lectureTitle());
        messageService.sendMessage(new MessageSendRequestDto(info.menteeId(), content), senderId);
    }

    @Transactional
    @Override
    public void rejectApplication(Long applicationId, String rejectReason, Long senderId) {
        ApplicationInfoDto info = validatePendingApplication(applicationId);
        applicationMapper.rejectApplication(applicationId);

        String content = String.format("'%s' 과외 신청이 반려되었습니다.\n사유: %s", info.lectureTitle(), rejectReason);
        messageService.sendMessage(new MessageSendRequestDto(info.menteeId(), content), senderId);
    }

    private ApplicationInfoDto validatePendingApplication(Long applicationId) {
        ApplicationInfoDto info = applicationMapper.findApplicationInfo(applicationId);
        if (info == null) {
            throw new ResourceNotFoundException("Application", applicationId);
        }

        String currentStatus = applicationMapper.findStatusByApplicationId(applicationId);
        if (!ApplicationStatus.PENDING.name().equals(currentStatus)) {
            throw new IllegalStateException("이미 처리된 과외 신청입니다.");
        }
        return info;
    }

    @Override
    public LectureApplyFormDto getLectureApplyForm(Long lectureId) {
        LectureApplyFormRawDto rawDto = applicationMapper.findLectureApplyFormData(lectureId);

        List<TimeSlot> timeSlots;
        try {
            timeSlots = objectMapper.readValue(
                    rawDto.availableTimeSlots(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, TimeSlot.class)
            );
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("시간 정보 파싱 실패", e);
        }

        return new LectureApplyFormDto(
                rawDto.lectureId(),
                rawDto.lectureTitle(),
                timeSlots,
                rawDto.profileImage(),
                rawDto.nickname(),
                rawDto.education(),
                rawDto.major(),
                rawDto.isCertified(),
                rawDto.averageRating()
        );
    }

    @Override
    @Transactional
    public void applyForLecture(LectureApplyRequestDto dto, Long menteeId) {
        String timeSlotsJson;
        try {
            timeSlotsJson = objectMapper.writeValueAsString(dto.requestedTimeSlots());
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("요청 시간대를 처리하는 중 오류 발생: " + e.getMessage());
        }

        applicationMapper.insertApplication(dto.lectureId(), menteeId, timeSlotsJson);

        // 3. 쪽지 전송
        LectureSimpleInfoDto info = applicationMapper.findLectureSimpleInfo(dto.lectureId());
        if (info == null) {
            throw new ResourceNotFoundException("Lecture", dto.lectureId());
        }

        String content = dto.message() == null || dto.message().trim().isEmpty()
                ? String.format("'%s' 과외 신청을 보냈습니다.", info.lectureTitle())
                : String.format("'%s' 과외 신청을 보냈습니다.\n%s", info.lectureTitle(), dto.message());

        messageService.sendMessage(
                new MessageSendRequestDto(info.mentorId(), content),
                menteeId
        );
    }
}