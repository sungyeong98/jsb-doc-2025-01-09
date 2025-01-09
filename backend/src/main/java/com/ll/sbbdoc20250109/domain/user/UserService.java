package com.ll.sbbdoc20250109.domain.user;

import com.ll.sbbdoc20250109.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthTokenService authTokenService;

    public long count() {
        return userRepository.count();
    }

    public SiteUser join(String username, String password, String nickname, String email) {
        userRepository
                .findByUsername(username)
                .ifPresent(_ -> {
                    throw new ServiceException("400-1", "이미 존재하는 ID입니다.");
                });

        SiteUser user = SiteUser.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .email(email)
                .build();

        return userRepository.save(user);
    }

    public Optional<SiteUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<SiteUser> findByApiKey(String apiKey) {
        return userRepository.findByApiKey(apiKey);
    }

    public Optional<SiteUser> findById(long id) {
        return userRepository.findById(id);
    }

    public String genAccessToken(SiteUser user) {
        return authTokenService.genAccessToken(user);
    }

    public String genAuthToken(SiteUser user) {
        return user.getApiKey() + " " + genAccessToken(user);
    }

    public SiteUser getUserFromAccessToken(String accessToken) {
        Map<String, Object> payload = authTokenService.payload(accessToken);

        if (payload == null) return null;

        long id = (long) payload.get("id");
        String username = (String) payload.get("username");

        SiteUser user = new SiteUser(id, username);

        return user;
    }

}
