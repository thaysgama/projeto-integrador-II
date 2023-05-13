package com.br.digitalmoneyhouse.core.security;


import com.br.digitalmoneyhouse.token.model.RefreshTokenEntity;
import com.br.digitalmoneyhouse.token.service.RefreshTokenService;
import com.br.digitalmoneyhouse.user.model.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Jwt token util.
 */
@Log4j2
@Component
public class JwtTokenUtil {

    private static final long TOKEN_EXPIRATION = 300_000; // 5 min
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 86_400_000; // 7 dias
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Autowired
    private RefreshTokenService refreshTokenService;

    /**
     * Generate access token string.
     *
     * @param request the request
     * @param user    the user
     * @return the string
     */
    public String generateAccessToken(HttpServletRequest request, UserEntity user) {
        return tokenBuilder(request, user, new Date(System.currentTimeMillis() + TOKEN_EXPIRATION), "access_token");
    }

    /**
     * Generate refresh token string.
     *
     * @param request the request
     * @param user    the user
     * @return the string
     */
    public String generateRefreshToken(HttpServletRequest request, UserEntity user) {
        Date expiration = new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION);
        String refreshToken = tokenBuilder(request, user,  expiration,"refresh_token");
        refreshTokenService.saveRefreshToken(new RefreshTokenEntity(user.getId(), refreshToken, expiration));
        return refreshToken;
    }

    /**
     * Validate jwt token boolean.
     *
     * @param authToken the auth token
     * @return the boolean
     */
    public boolean validateJwtToken(String authToken)  {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(authToken);
            if("access_token".equals(claims.getBody().getId())) {
                return true;
            }
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token", ex);
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token", ex);
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token", ex);
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.", ex);
        }
        return false;
    }

    /**
     * Gets subject.
     *
     * @param token the token
     * @return the subject
     */
    public String getSubject(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String tokenBuilder(HttpServletRequest request, UserEntity user, Date expiration, String type) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setId(type)
                .setIssuer(request.getRequestURL().toString())
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(SECRET_KEY)
                .compact();
    }

    public Map<String, String> genarateTokens(HttpServletRequest request, UserEntity user, String refreshTokenKeep) {

        String accessToken = generateAccessToken(request,user);
        String refreshToken = refreshTokenKeep != null ? refreshTokenKeep : generateRefreshToken(request,user);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        return tokens;
    }

}
