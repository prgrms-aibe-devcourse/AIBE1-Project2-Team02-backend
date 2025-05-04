package aibe1.proj2.mentoss.feature.application.service;

import aibe1.proj2.mentoss.feature.application.model.dto.ApplicationInfoDto;
import aibe1.proj2.mentoss.feature.application.model.dto.AppliedLectureResponseDto;
import aibe1.proj2.mentoss.feature.application.model.dto.LectureApplicantDto;
import aibe1.proj2.mentoss.feature.application.model.dto.LectureResponseDto;
import aibe1.proj2.mentoss.feature.application.model.mapper.ApplicationMapper;
import aibe1.proj2.mentoss.feature.message.model.dto.MessageSendRequestDto;
import aibe1.proj2.mentoss.feature.message.service.MessageService;
import aibe1.proj2.mentoss.global.entity.enums.ApplicationStatus;
import aibe1.proj2.mentoss.global.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationMapper applicationMapper;
    private final MessageService messageService;

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
}