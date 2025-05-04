package aibe1.proj2.mentoss.global.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(Authentication authentication, List<String> roles, Long userId) {
        String username = authentication.getName();
        Instant now = Instant.now();
        Date expiration = new Date(now.toEpochMilli() + expirationMs);

        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("roles", roles);

        if (userId != null) {
            claimsMap.put("userId", userId);
        }

        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(expiration)
                .claims(claimsMap)
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    public Long getUserId(String token) {
        Object userIdObj = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userId");

        if (userIdObj != null) {
            if(userIdObj instanceof Integer) {
                return ((Integer) userIdObj).longValue();
            } else if (userIdObj instanceof Long) {
                return (Long) userIdObj;
            } else {
                try {
                    return Long.valueOf(userIdObj.toString());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }

        return null;
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> getRoles(String token) {
        return (List<String>) Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("roles");
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        Long userId = getUserId(token);

        List<SimpleGrantedAuthority> authorities = getRoles(token).stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        UserDetails userDetails;

        if(userId != null) {
            userDetails = new CustomUserDetails(username, "", authorities, userId);
        } else {
            userDetails = new User(username, "", authorities);
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}