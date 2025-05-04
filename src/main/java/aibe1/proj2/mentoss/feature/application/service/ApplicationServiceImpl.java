package aibe1.proj2.mentoss.feature.application.service;

import aibe1.proj2.mentoss.feature.application.model.dto.AppliedLectureResponseDto;
import aibe1.proj2.mentoss.feature.application.model.dto.LectureApplicantDto;
import aibe1.proj2.mentoss.feature.application.model.dto.LectureResponseDto;
import aibe1.proj2.mentoss.feature.application.model.mapper.ApplicationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationMapper applicationMapper;

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
}