package aibe1.proj2.mentoss.feature.account.service;

import aibe1.proj2.mentoss.feature.account.model.dto.ProfileResponseDto;
import aibe1.proj2.mentoss.feature.account.model.dto.ProfileUpdateRequestDto;
import aibe1.proj2.mentoss.feature.account.model.mapper.AccountMapper;
import aibe1.proj2.mentoss.global.entity.AppUser;
import aibe1.proj2.mentoss.global.entity.Region;
import aibe1.proj2.mentoss.global.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountMapper accountMapper;

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
}
