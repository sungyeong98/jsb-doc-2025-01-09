package com.ll.sbbdoc20250109.domain.user;

import com.ll.sbbdoc20250109.standard.util.Ut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthTokenService {

    @Value("${custom.jwt.secretKey}")
    private String jwtSecretKey;

    @Value("${custom.accessToken.expirationSeconds}")
    private long accessTokenExpirationSeconds;

    String genAccessToken(SiteUser user) {
        long id = user.getId();
        String username = user.getUsername();

        return Ut.jwt.toString(
                jwtSecretKey,
                accessTokenExpirationSeconds,
                Map.of("id", id, "username", username)
        );
    }

    Map<String, Object> payload(String accessToken) {
        Map<String, Object> parsePayload = Ut.jwt.payload(jwtSecretKey, accessToken);

        if (parsePayload == null) return null;

        long id = (long) (Integer) parsePayload.get("id");
        String username = (String) parsePayload.get("username");

        return Map.of("id", id, "username", username);
    }

}
