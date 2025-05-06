package aibe1.proj2.mentoss.feature.account.service;

import aibe1.proj2.mentoss.feature.account.model.dto.*;
import aibe1.proj2.mentoss.feature.account.model.mapper.AccountMapper;
import aibe1.proj2.mentoss.feature.account.model.mapper.MentorMapper;
import aibe1.proj2.mentoss.feature.file.service.FileService;
import aibe1.proj2.mentoss.feature.region.model.dto.RegionDto;
import aibe1.proj2.mentoss.global.entity.AppUser;
import aibe1.proj2.mentoss.global.entity.MentorProfile;
import aibe1.proj2.mentoss.global.entity.Region;
import aibe1.proj2.mentoss.global.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountMapper accountMapper;
    private final FileService fileService;
    private final MentorMapper mentorMapper;

    @Override
    public ProfileResponseDto getProfile(Long userId) {
        AppUser appUser = accountMapper.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("AppUser", userId));

        List<Region> regions = accountMapper.findRegionByUserId(userId);

        return ProfileResponseDto.fromEntity(appUser, regions);
    }

    @Override
    @Transactional
    public void updateProfile(Long userId, ProfileUpdateRequestDto requestDto) {
        AppUser appUser = accountMapper.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("AppUser", userId));

        Long age = calculateAge(requestDto.birthDate());

        appUser.setNickname(requestDto.nickname());
        appUser.setBirthDate(requestDto.birthDate());
        appUser.setAge(age);
        appUser.setSex(requestDto.sex());
        appUser.setMbti(requestDto.mbti());

        // 대표 지역 설정 (첫번째 지역이 대표 지역)
        if (requestDto.regionCodes() != null && !requestDto.regionCodes().isEmpty()) {
            String firstRegionCode = requestDto.regionCodes().get(0);
            Optional<Region> region = accountMapper.findRegionByRegionCode(firstRegionCode);
            if (region.isPresent()) {
                appUser.setRegionCode(firstRegionCode);
            }
        }

        accountMapper.updateProfile(appUser);

        if (requestDto.regionCodes() != null && !requestDto.regionCodes().isEmpty()) {
            accountMapper.deleteUserRegion(userId);

            for (String regionCode : requestDto.regionCodes()) {
                if(accountMapper.findRegionByRegionCode(regionCode).isPresent()){
                    accountMapper.insertUserRegion(userId, regionCode);
                }
            }
        }
    }


    @Override
    public Long calculateAge(String birthDate) {
        if (birthDate == null || birthDate.length() != 8) {
            return null;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate birthLocalDate = LocalDate.parse(birthDate, formatter);

            LocalDate currentDate = LocalDate.now();
            return (long) Period.between(birthLocalDate, currentDate).getYears();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean isProfileCompleted(Long userId) {
        AppUser appUser = accountMapper.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("AppUser", userId));

        return appUser.getNickname() != null &&
                appUser.getBirthDate() != null &&
                appUser.getSex() != null;
    }

    @Override
    public void deleteAccount(Long userId) {
        AppUser appUser = accountMapper.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("AppUser", userId));

        appUser.setIsDeleted(true);
        appUser.setDeletedAt(LocalDateTime.now());

        accountMapper.softDeleteUser(appUser);

        accountMapper.deleteUserRegion(userId);
    }

    @Override
    @Transactional
    public String updateProfileImage(Long userId, MultipartFile file) throws IOException {
        AppUser appUser = accountMapper.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("AppUser", userId));

        if (appUser.getProfileImage() != null && !appUser.getProfileImage().isEmpty()) {
            fileService.deleteFile(appUser.getProfileImage());
        }

        String imageUrl = fileService.uploadFile(file, "profiles");

        accountMapper.updateProfileImage(userId, imageUrl);

        return imageUrl;
    }

    @Override
    public boolean isMentor(Long userId) {
        return mentorMapper.existsByUserId(userId);
    }

    @Override
    public MentorProfileResponseDto getMentorProfile(Long userId) {
        MentorProfile mentorProfile = mentorMapper.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("MentorProfile", userId));

        return MentorProfileResponseDto.fromEntity(mentorProfile);
    }

    @Override
    @Transactional
    public void applyMentorProfile(Long userId, MentorProfileRequestDto requestDto) throws IOException {
        if (mentorMapper.existsByUserId(userId)) {
            throw new IllegalArgumentException("이미 멘토 신청을 하였습니다");
        }

        if (requestDto.content() == null || requestDto.content().length() < 10) {
            throw new IllegalArgumentException("자기소개는 최소 10자 이상 작성해주세요");
        }

        String appealFileUrl = null;
        if (requestDto.appealFile() != null && !requestDto.appealFile().isEmpty()) {
            appealFileUrl = fileService.uploadFile(requestDto.appealFile(), "mentor-appeals");
        }

        MentorProfile mentorProfile = MentorProfile.builder()
                .userId(userId)
                .content(requestDto.content())
                .appealFileUrl(appealFileUrl)
                .isCertified(false)
                .createdAt(LocalDateTime.now())
                .build();

        mentorMapper.insertMentorProfile(mentorProfile);
        mentorMapper.updateToMentorRole(userId);
    }

    @Override
    public void updateMentorProfile(Long userId, MentorProfileRequestDto requestDto) throws IOException {
        MentorProfile mentorProfile = mentorMapper.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("MentorProfile", userId));

        if (requestDto.content() == null || requestDto.content().length() < 10) {
            throw new IllegalArgumentException("자기소개는 최소 10자 이상 작성해주세요");
        }

        String appealFileUrl = null;
        if (requestDto.appealFile() != null && !requestDto.appealFile().isEmpty()){
            if (mentorProfile.getAppealFileUrl() != null && !mentorProfile.getAppealFileUrl().isEmpty()) {
                fileService.deleteFile(mentorProfile.getAppealFileUrl());
            }

            appealFileUrl = fileService.uploadFile(requestDto.appealFile(), "mentor-appeals");
            mentorProfile.setAppealFileUrl(appealFileUrl);
        }

        mentorProfile.setContent(requestDto.content());
        mentorMapper.updateMentorProfile(mentorProfile);
    }

    @Override
    public MentorStatusResponseDto getMentorStatus(Long userId) {
        boolean isMentor = mentorMapper.existsByUserId(userId);
        boolean isCertified = false;

        if (isMentor) {
            MentorProfile mentorProfile = mentorMapper.findByUserId(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("MentorProfile", userId));
            isCertified = mentorProfile.getIsCertified();
        }

        return new MentorStatusResponseDto(isMentor, isCertified);
    }

    @Override
    public List<RegionDto> getUserRegions(Long userId) {
        AppUser appUser = accountMapper.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("AppUser", userId));
        List<Region> regions = accountMapper.findRegionByUserId(userId);

        return regions.stream()
                .map(region -> new RegionDto(
                        region.getRegionCode(),
                        region.getSido(),
                        region.getSigungu(),
                        region.getDong(),
                        formatRegionDisplayName(region)
                ))
                .collect(Collectors.toList());
    }

    private String formatRegionDisplayName(Region region) {
        StringBuilder sb = new StringBuilder();

        if (region.getSido() != null) {
            sb.append(region.getSido());
        }

        if (region.getSigungu() != null) {
            sb.append(" ").append(region.getSigungu());
        }

        if (region.getDong() != null) {
            sb.append(" ").append(region.getDong());
        }

        return sb.toString();
    }


    @Override
    public void updateUserRegion(Long userId, UserRegionsUpdateRequestDto regionDto) {
        AppUser appUser = accountMapper.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("AppUser", userId));

        accountMapper.deleteUserRegion(userId);

        if (regionDto.regionCodes() == null || regionDto.regionCodes().isEmpty()) {
            return;
        }

        String firstRegionCode = regionDto.regionCodes().get(0);
        Optional<Region> region = accountMapper.findRegionByRegionCode(firstRegionCode);
        if (region.isPresent()) {
            appUser.setRegionCode(firstRegionCode);
            accountMapper.updateProfile(appUser);
        }

        for(String regionCode : regionDto.regionCodes()) {
            if (accountMapper.findRegionByRegionCode(regionCode).isPresent()) {
                accountMapper.insertUserRegion(userId, regionCode);
            }
        }
    }
}
