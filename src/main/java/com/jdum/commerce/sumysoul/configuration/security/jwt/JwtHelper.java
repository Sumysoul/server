package com.jdum.commerce.sumysoul.configuration.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtHelper {

  public static final String AUTHORITIES_KEY = "auth";

  @Value("${application.auth.jwt-secret}")
  private String jwtSecret;

  @Value("${application.auth.jwt-expiration-ms}")
  private int jwtExpirationMs;

  private SecretKey secretKey;

  @PostConstruct
  void init() {
    this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  public String generateJwtToken(Authentication authentication) {
    return Jwts.builder()
        .subject(authentication.getName())
        .issuedAt(new Date())
        .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .claim(AUTHORITIES_KEY, getAuthorities(authentication))
        .signWith(secretKey)
        .compact();
  }

  private String getAuthorities(Authentication authentication) {
    return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(
        Collectors.joining(","));
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload().getSubject();
  }

  public boolean isJwtValid(String authToken) {
    try {
      Jwts.parser().verifyWith(secretKey).build().parse(authToken);
      return true;
    } catch (MalformedJwtException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      log.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }
}
