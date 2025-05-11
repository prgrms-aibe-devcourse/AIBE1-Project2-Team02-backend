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
import org.springframework.security.oauth2.core.OAuth2Error;
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
        log.info("OAuth2 로그인 시도: provider={}", provider);

        String providerId = getProviderId(oAuth2User, provider);
        String email = getEmail(oAuth2User, provider);
        String nickname = getNickname(oAuth2User, provider);
        String profileImage = getProfileImage(oAuth2User, provider);

        if (email != null && !email.isEmpty()) {
            Optional<AppUser> userByEmail = appUserMapper.findByEmail(email);

            if (userByEmail.isPresent()) {
                AppUser existingEmailUser = userByEmail.get();
                if (!existingEmailUser.getProvider().equals(provider)) {
                    String errorMessage = "이미 " + existingEmailUser.getProvider() +
                            " 계정으로 가입된 이메일입니다. 해당 소셜 계정으로 로그인해주세요.";
                    log.info("이메일 중복 확인: {}", errorMessage);

                    throw new OAuth2AuthenticationException(
                            new OAuth2Error("email_already_in_use"), errorMessage);
                }
            }
        }

        try {
            saveOrUpdateUser(provider, providerId, email, nickname, profileImage);
        } catch (Exception e) {
            String errorMessage = "이미 사용 중인 이메일입니다. 다른 소셜 계정으로 로그인해주세요.";
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("user_registration_error"), errorMessage);
        }

        return oAuth2User;
    }

    private void saveOrUpdateUser(String provider, String providerId, String email, String nickname, String profileImage) {
        Optional<AppUser> existingUser = appUserMapper.findByProviderAndProviderId(provider, providerId);

        if (existingUser.isPresent()) {
            log.info("기존 사용자 로그인: userId={}", existingUser.get().getUserId());
            return;
        }

        if (email != null && !email.isEmpty()) {
            Optional<AppUser> userByEmail = appUserMapper.findByEmail(email);

            if (userByEmail.isPresent()) {
                AppUser existingEmailUser = userByEmail.get();
                log.warn("이미 다른 소셜 계정({}:{}으로 가입된 이메일입니다: {}",
                        existingEmailUser.getProvider(),
                        existingEmailUser.getProviderId(),
                        email);

                throw new RuntimeException(
                        "이미 " + existingEmailUser.getProvider() + " 계정으로 가입된 이메일입니다. " +
                                "해당 소셜 계정으로 로그인해주세요.");
            }
        }

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

        try {
            appUserMapper.save(newUser);
            log.info("신규 사용자 등록: userId={}, 닉네임={}, 이메일={}",
                    newUser.getUserId(), uniqueNickname, email);
        } catch (Exception e) {
            log.info("사용자 저장 중 오류 발생: {}", e.getMessage());

            if (e.getMessage() != null && e.getMessage().contains("Duplicate entry") &&
                    (e.getMessage().contains("for key 'email'") || e.getMessage().contains("for key 'app_user.email'"))) {
                throw new RuntimeException("이미 사용 중인 이메일입니다. 다른 소셜 계정으로 로그인해주세요.");
            } else {
                throw new RuntimeException("사용자 등록 중 오류가 발생했습니다.", e);
            }
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