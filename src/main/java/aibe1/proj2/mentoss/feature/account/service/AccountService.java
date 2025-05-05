package aibe1.proj2.mentoss.feature.account.service;

import aibe1.proj2.mentoss.feature.account.model.dto.MentorProfileResponseDto;
import aibe1.proj2.mentoss.feature.account.model.dto.ProfileResponseDto;
import aibe1.proj2.mentoss.feature.account.model.dto.ProfileUpdateRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AccountService {

    ProfileResponseDto getProfile(Long userId);

    void updateProfile(Long userId, ProfileUpdateRequestDto requestDto);

    Long calculateAge(String birthDate);

    boolean isProfileCompleted(Long userId);

    void deleteAccount(Long userId);

    String updateProfileImage(Long userId, MultipartFile file) throws IOException;

    boolean isMentor(Long userId);

    MentorProfileResponseDto getMentorProfile(Long userId);
}
