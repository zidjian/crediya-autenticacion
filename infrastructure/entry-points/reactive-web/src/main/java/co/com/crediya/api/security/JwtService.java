package co.com.crediya.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@Slf4j
public class JwtService {

    private final String secretKey;
    private final long expirationTimeInHours;

    public JwtService(
            @Value("${jwt.secret:mySecretKey123456789012345678901234567890}") String secretKey,
            @Value("${jwt.expiration:24}") long expirationTimeInHours
    ) {
        this.secretKey = secretKey;
        this.expirationTimeInHours = expirationTimeInHours;
    }

    public String generateToken(Long userId, String email, String nombre, String apellido, String rolNombre) {
        Instant now = Instant.now();
        Instant expiration = now.plus(expirationTimeInHours, ChronoUnit.HOURS);

        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .claim("nombre", nombre)
                .claim("apellido", apellido)
                .claim("rol", rolNombre)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public Claims validateToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("Error validating JWT token: {}", e.getMessage());
            throw new RuntimeException("Token inválido", e);
        }
    }

    public String getEmailFromToken(String token) {
        return validateToken(token).getSubject();
    }

    public Long getUserIdFromToken(String token) {
        return validateToken(token).get("userId", Long.class);
    }

    public String getRolFromToken(String token) {
        return validateToken(token).get("rol", String.class);
    }

    public boolean isTokenExpired(String token) {
        try {
            return validateToken(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
