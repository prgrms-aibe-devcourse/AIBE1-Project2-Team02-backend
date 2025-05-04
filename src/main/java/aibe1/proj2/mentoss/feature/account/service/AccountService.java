package aibe1.proj2.mentoss.feature.account.service;

import aibe1.proj2.mentoss.feature.account.model.dto.ProfileResponseDto;
import aibe1.proj2.mentoss.feature.account.model.dto.ProfileUpdateRequestDto;

public interface AccountService {

    ProfileResponseDto getProfile(Long userId);

}
