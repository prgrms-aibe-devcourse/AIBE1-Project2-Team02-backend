package aibe1.proj2.mentoss.feature.account.service;

import aibe1.proj2.mentoss.feature.account.model.dto.*;
import aibe1.proj2.mentoss.feature.application.model.dto.LectureResponseDto;
import aibe1.proj2.mentoss.feature.region.model.dto.RegionDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AccountService {

    ProfileResponseDto getProfile(Long userId);

    void updateProfile(Long userId, ProfileUpdateRequestDto requestDto);

    Long calculateAge(String birthDate);

    boolean isProfileCompleted(Long userId);

    void deleteAccount(Long userId);

    String updateProfileImage(Long userId, MultipartFile file) throws IOException;

    boolean isMentor(Long userId);

    MentorProfileResponseDto getMentorProfile(Long userId);

    MentorPublicProfileDto getMentorPublicProfile(Long mentorId);

    void applyMentorProfile(Long userId, MentorProfileRequestDto requestDto) throws IOException;

    void updateMentorProfile(Long userId, MentorProfileRequestDto requestDto) throws IOException;

    MentorStatusResponseDto getMentorStatus(Long userId);

    List<RegionDto> getUserRegions(Long userId);

    void updateUserRegion(Long userId, UserRegionsUpdateRequestDto regionDto);

    void deleteProfileImage(Long userId);

    MyLectureResponseDto getMyLecture(Long userId);
}
