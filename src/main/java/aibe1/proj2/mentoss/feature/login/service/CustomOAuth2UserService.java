package aibe1.proj2.mentoss.feature.login.service;

import aibe1.proj2.mentoss.feature.login.model.mapper.AppUserMapper;
import aibe1.proj2.mentoss.global.entity.AppUser;
import aibe1.proj2.mentoss.global.entity.enums.EntityStatus;
import aibe1.proj2.mentoss.global.entity.enums.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final AppUserMapper appUserMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();

        String providerId = getProviderId(oAuth2User, provider);
        String email = getEmail(oAuth2User, provider);
        String nickname = getNickname(oAuth2User, provider);
        String profileImage = getProfileImage(oAuth2User, provider);

        saveOrUpdateUser(provider, providerId, email, nickname, profileImage);

        log.info("소셜 로그인 성공: provider={}, providerId={}, email={}", provider, providerId, email);

        return oAuth2User;
    }

    private void saveOrUpdateUser(String provider, String providerId, String email, String nickname, String profileImage) {
        Optional<AppUser> existingUser = appUserMapper.findByProviderAndProviderId(provider, providerId);

        if (existingUser.isPresent()) {
            log.info("기존 사용자 로그인 : userId={}", existingUser.get().getUserId());
        } else {
            String uniqueNickname = generateUniqueRandomNickname(nickname);

            AppUser newUser = AppUser.builder()
                    .provider(provider)
                    .providerId(providerId)
                    .email(email)
                    .nickname(uniqueNickname)
                    .profileImage(profileImage)
                    .role(UserRole.MENTEE.name())
                    .status(EntityStatus.AVAILABLE.name())
                    .reportCount(0L)
                    .isDeleted(false)
                    .build();

            appUserMapper.save(newUser);
            log.info("신규 사용자 등록 : userId={}, 닉네임={}", newUser.getUserId(), uniqueNickname);
        }
    }

    private String generateUniqueRandomNickname(String originalNickname) {
        if (originalNickname == null || originalNickname.isEmpty()) {
            originalNickname = "멘티";
        }

        String uniqueNickname;
        int maxAttempts = 10;
        int attempts = 0;

        do {
            int randomNum = (int) (Math.random() * 9000) + 1000;
            uniqueNickname = originalNickname + randomNum;
            attempts++;

            if (attempts > maxAttempts) {
                uniqueNickname = originalNickname + System.currentTimeMillis();
                break;
            }
        } while (appUserMapper.nicknameExists(uniqueNickname));

        return uniqueNickname;
    }

    private String getProviderId(OAuth2User oAuth2User, String provider) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        if("google".equals(provider)) {
            return (String) attributes.get("sub");
        } else if ("kakao".equals(provider)) {
            return String.valueOf(attributes.get("id"));
        }

        return null;
    }

    private String getEmail(OAuth2User oAuth2User, String provider) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        if("google".equals(provider)) {
            return (String) attributes.get("email");
        } else if ("kakao".equals(provider)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            return kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
        }

        return null;
    }

    private String getNickname(OAuth2User oAuth2User, String provider) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        if ("google".equals(provider)) {
            return (String) attributes.get("name");
        } else if ("kakao".equals(provider)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            if (kakaoAccount != null) {
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                return profile != null ? (String) profile.get("nickname") : null;
            }
        }

        return null;
    }

    private String getProfileImage(OAuth2User oAuth2User, String provider) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        if("google".equals(provider)) {
            return (String) attributes.get("picture");
        } else if ("kakao".equals(provider)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            if (kakaoAccount != null) {
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                return profile != null ? (String) profile.get("profile_image_url") : null;
            }
        }

        return null;
    }
}