package com.aymen.saas.security;

import com.aymen.saas.exceptions.UnauthorizedException;
import com.aymen.saas.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenService {

    private final JwtProperties jwtProperties;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        try {
            this.privateKey = loadPrivateKey(this.jwtProperties.getPrivateKeyPath());
            this.publicKey = loadPublicKey(this.jwtProperties.getPublicKeyPath());

            log.info("Private and Public key loaded successfully.");
        } catch (final Exception e) {
            log.error("Error while loading private and public key.");
            throw new RuntimeException("Error loading private and public key.");
        }
    }



    // generate a token
    public String generateAccessToken(
            @NonNull
            final String tenantId,
            @NonNull
            final String userId,
            final String role
    ) {

        final Date now = new Date();
        final Date expiration = new Date(System.currentTimeMillis() + this.jwtProperties.getAccessTokenExpiration());

        // generate token with Jwts
        return Jwts.builder()
                .subject(userId)
                .claim("tenant_id",tenantId)
                .claim("role",role)
                .issuedAt(now)
                .expiration(expiration)
                .issuer("stock-saas-app")
                .signWith(this.privateKey, Jwts.SIG.RS256)
                .compact();
    }

    public String getUserIdFromToken(final String token) {
        final Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    public String getTenantIdFRomToken(final String token) {
        final Claims claims = getClaimsFromToken(token);
        return claims.get("tenant_id", String.class);
    }

    public String getRoleFromToken(final String token) {
        final Claims claims = getClaimsFromToken(token);
        return claims.get("role", String.class);
    }



    public boolean validateToken(final String token) {
        try {
            Jwts.parser()
                    .verifyWith(this.publicKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        }catch (final ExpiredJwtException e) {
            throw new UnauthorizedException("Token has expired");
        } catch (final UnsupportedOperationException e){
            throw new UnauthorizedException("Token is not signed");
        } catch (final MalformedJwtException e){
            throw new UnauthorizedException("Token is malformed");
        } catch (final SecurityException e){
            throw new UnauthorizedException("Invalid JWT signature");
        } catch (IllegalArgumentException e){
            throw new UnauthorizedException("JWT claims string is empty");
        }
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(this.publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private PrivateKey loadPrivateKey(String privateKeyPath) throws Exception {
        try (final InputStream is = JwtTokenService.class.getClassLoader().getResourceAsStream(privateKeyPath)) {
            if(is == null) {
                throw new RuntimeException("Private key  not found.");
            }
            final String key = new String(is.readAllBytes());
            final String privateKeyPEM = key
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            final byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
            final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
        }
    }

    private PublicKey loadPublicKey(String publicKeyPath) throws Exception {
        try (final InputStream is = JwtTokenService.class.getClassLoader().getResourceAsStream(publicKeyPath)) {
            if(is == null) {
                throw new RuntimeException("Public key  not found.");
            }
            final String key = new String(is.readAllBytes());
            final String privateKeyPEM = key
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            final byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
            final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            return KeyFactory.getInstance("RSA").generatePublic(keySpec);
        }
    }
























}
