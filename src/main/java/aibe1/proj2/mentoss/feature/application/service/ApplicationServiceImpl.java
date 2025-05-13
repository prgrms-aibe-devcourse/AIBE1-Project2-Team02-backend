package aibe1.proj2.mentoss.feature.application.service;

import aibe1.proj2.mentoss.feature.application.model.dto.*;
import aibe1.proj2.mentoss.feature.application.model.mapper.ApplicationMapper;
import aibe1.proj2.mentoss.feature.message.model.dto.MessageSendRequestDto;
import aibe1.proj2.mentoss.feature.message.service.MessageService;
import aibe1.proj2.mentoss.feature.notification.service.NotificationService;
import aibe1.proj2.mentoss.global.entity.Notification;
import aibe1.proj2.mentoss.global.entity.enums.ApplicationStatus;
import aibe1.proj2.mentoss.global.entity.enums.EntityStatus;
import aibe1.proj2.mentoss.global.entity.enums.NotificationType;
import aibe1.proj2.mentoss.global.exception.ResourceNotFoundException;
import aibe1.proj2.mentoss.global.exception.application.DuplicateApplicationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationMapper applicationMapper;
    private final MessageService messageService;
    private final NotificationService notificationService;
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
    public List<LectureApplicantDto> getApplicantsByLecture(Long userId) {
        List<LectureApplicantRawDto> rawDtos = applicationMapper.findApplicantsByLecture(userId);

        return rawDtos.stream().map(rawDto -> {
            TimeSlot timeSlot;
            try {
                List<TimeSlot> timeSlotList = objectMapper.readValue(
                        rawDto.requestedTimeSlots(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, TimeSlot.class)
                );

                timeSlot = timeSlotList.isEmpty() ? null : timeSlotList.get(0);

            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("신청 시간 정보 파싱 실패", e);
            }

            return new LectureApplicantDto(
                    rawDto.applicationId(),
                    rawDto.nickname(),
                    rawDto.lectureTitle(),
                    rawDto.createdAt(),
                    rawDto.profileImage(),
                    timeSlot
            );
        }).toList();
    }

    @Transactional
    @Override
    public void approveApplication(Long applicationId, Long senderId) {
        LocalDateTime time = LocalDateTime.now();
        ApplicationInfoDto info = validatePendingApplication(applicationId);

        int updatedRows = applicationMapper.acceptApplication(applicationId, time);

        if (updatedRows == 0) {
            throw new IllegalStateException("과외 수락 실패: 이미 수락되었거나 잘못된 요청입니다.");
        }

        applicationMapper.insertLectureMentee(applicationId, time);

        String content = String.format("'%s' 과외 신청이 수락되었습니다.", info.lectureTitle());
        messageService.sendMessage(new MessageSendRequestDto(info.menteeId(), content, true), senderId);

        String notificationContent = String.format("\"%s\" 신청을 수락했어요.", info.lectureTitle());
        notificationService.createNotification(
                info.menteeId(),                  // receiverId
                senderId,                         // senderId
                NotificationType.RESPONSE.name(), // type
                notificationContent,             // content
                applicationId,                   // referenceId
                "/questions"                   // targetUrl (멘티가 신청 내역 확인하는 경로)
        );
    }

    @Transactional
    @Override
    public void rejectApplication(Long applicationId, String rejectReason, Long senderId) {
        LocalDateTime time = LocalDateTime.now();
        ApplicationInfoDto info = validatePendingApplication(applicationId);
        applicationMapper.rejectApplication(applicationId, time);

        String content = String.format("'%s' 과외 신청이 반려되었습니다.\n사유: %s", info.lectureTitle(), rejectReason);
        messageService.sendMessage(new MessageSendRequestDto(info.menteeId(), content, true), senderId);

        String notificationContent = String.format("\"%s\" 신청을 반려했어요.", info.lectureTitle());
        notificationService.createNotification(
                info.menteeId(),                  // receiverId
                senderId,                         // senderId
                NotificationType.RESPONSE.name(), // type
                notificationContent,             // content
                applicationId,                   // referenceId
                "/questions"                   // targetUrl (멘티가 신청 내역 확인하는 경로)
        );
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

    @Transactional
    @Override
    public void updateLectureStatus(Long lectureId, boolean isClosed, Long userId) {
        LectureStatusDto lecture = applicationMapper.findLectureStatusById(lectureId);

        if (lecture == null) {
            throw new ResourceNotFoundException("Lecture", lectureId);
        }

        // 강의가 BANNED/SUSPENDED 상태라면 변경 불가
        if (!EntityStatus.AVAILABLE.name().equals(lecture.status())) {
            throw new IllegalStateException("해당 강의는 정지 되었습니다.");
        }

        // 요청한 사용자가 강의 소유자가 아닌 경우
        if (!lecture.userId().equals(userId)) {
            throw new IllegalStateException("해당 강의에 대한 권한이 없습니다.");
        }

        // 강의 마감시 신청자 존재 여부 체크
        if (isClosed) {
            int pendingCount = applicationMapper.countPendingApplicationsByLectureId(lectureId);
            if (pendingCount > 0) {
                throw new IllegalStateException("해당 강의에 대한 신청자가 존재합니다.");
            }
        }

        applicationMapper.updateLectureStatus(lectureId, isClosed);
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

        int count = applicationMapper.countDuplicateApplication(dto.lectureId(), menteeId);

        if (count > 0) {
            throw new DuplicateApplicationException("[DUPLICATE_APPLICATION]");
        }

        String timeSlotsJson;
        try {
            timeSlotsJson = objectMapper.writeValueAsString(dto.requestedTimeSlots());
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("요청 시간대를 처리하는 중 오류 발생: " + e.getMessage());
        }
        LocalDateTime time = LocalDateTime.now();
        applicationMapper.insertApplication(dto.lectureId(), menteeId, timeSlotsJson, time);

        // 3. 쪽지 전송
        LectureSimpleInfoDto info = applicationMapper.findLectureSimpleInfo(dto.lectureId());
        if (info == null) {
            throw new ResourceNotFoundException("Lecture", dto.lectureId());
        }

        String content = dto.message() == null || dto.message().trim().isEmpty()
                ? String.format("'%s' 과외에 새로운 신청이 있습니다.", info.lectureTitle())
                : String.format("'%s' 과외에 새로운 신청이 있습니다.\n%s", info.lectureTitle(), dto.message());

        messageService.sendMessage(
                new MessageSendRequestDto(info.mentorId(), content, true),
                menteeId
        );

        String notificationContent = String.format("\"%s\" 새로운 신청이 도착했어요.", info.lectureTitle());

        notificationService.createNotification(
                info.mentorId(),                    // 알림 받을 사람 (멘토)
                menteeId,                           // 알림 보낸 사람 (멘티)
                NotificationType.APPLICATION.name(), // "APPLICATION"
                notificationContent,                // 내용
                dto.lectureId(),                    // referenceId
                "/questions"                     // targetUrl
        );
    }

    @Override
    public List<MenteeResponseDto> getMatchedMenteesByMentor(Long mentorId) {
        return applicationMapper.findMatchedMenteesByMentorId(mentorId);
    }

    @Override
    public void cancelApplication(Long applicationId, Long userId) {
        ApplicationInfoDto info = applicationMapper.findApplicationInfo(applicationId);

        if (info == null) {
            throw new ResourceNotFoundException("Application", applicationId);
        }

        String currentStatus = applicationMapper.findStatusByApplicationId(applicationId);
        if(!ApplicationStatus.APPROVED.name().equals(currentStatus)) {
            throw new IllegalStateException("취소할 수 있는 상태가 아닙니다: " + currentStatus);
        }

        Long lectureId = applicationMapper.findLectureIdByApplicationId(applicationId);

        LectureSimpleInfoDto lectureInfo = applicationMapper.findLectureSimpleInfo(lectureId);
        if (lectureInfo == null) {
            throw new ResourceNotFoundException("Lecture", lectureId);
        }

        Long mentorId = lectureInfo.mentorId();

        if (!mentorId.equals(userId) && !info.menteeId().equals(userId)) {
            throw new IllegalStateException("매칭 취소 권한이 없습니다.");
        }

        LocalDateTime currentTime = LocalDateTime.now();
        applicationMapper.updateApplicationStatus(applicationId, ApplicationStatus.CANCELLED.name(), currentTime);

        String content;
        String cancelReson = mentorId.equals(userId) ? "멘토 사정으로 인해" : "멘티 사정으로 인해";

        if (mentorId.equals(userId)) {
            content = String.format("'%s' 과외 매칭이 %s 취소되었습니다.", info.lectureTitle(), cancelReson);
            messageService.sendMessage(new MessageSendRequestDto(info.menteeId(), content, true), userId);
        } else {
            content = String.format("'%s' 과외 매칭이 %s 취소되었습니다.", info.lectureTitle(), cancelReson);
            messageService.sendMessage(new MessageSendRequestDto(mentorId, content, true), userId);
        }

        String notificationContent = String.format("\"%s\" 과외 매칭이 취소되었어요.", info.lectureTitle());
        notificationService.createNotification(
                info.menteeId(),
                userId,
                NotificationType.RESPONSE.name(),
                notificationContent,
                applicationId,
                "/questions"
        );
    }
}