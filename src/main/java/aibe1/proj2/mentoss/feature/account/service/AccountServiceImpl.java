package aibe1.proj2.mentoss.feature.account.service;

import aibe1.proj2.mentoss.feature.account.model.dto.ProfileResponseDto;
import aibe1.proj2.mentoss.feature.account.model.mapper.AccountMapper;
import aibe1.proj2.mentoss.global.entity.AppUser;
import aibe1.proj2.mentoss.global.entity.Region;
import aibe1.proj2.mentoss.global.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
