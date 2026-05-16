package com.tmdt.shop_service.utils;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.openid.connect.sdk.federation.utils.JWTUtils;
import com.tmdt.shop_service.core.exception.InternalException;
import com.tmdt.shop_service.core.exception.ResourceNotFoundException;
import com.tmdt.shop_service.modules.auth.CustomUserDetail;
import com.tmdt.shop_service.modules.users.application.service.UserRoleService;
import com.tmdt.shop_service.modules.users.application.service.UserService;
import com.tmdt.shop_service.modules.users.domain.model.Role;
import com.tmdt.shop_service.modules.users.domain.model.Users;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

@Slf4j
@Component
public class JwtUtils {
    private final String privateKeyPath;
    private final String publicKeyPath;
    private final String privateKey;
    private final RSAPublicKey rsaPublicKey;
    @Getter private final Long expriry;
    @Getter private final String publicKey;
    private final UserRoleService userRoleService;
    private final UserService userService;

    public JwtUtils(
            @Value("${jwt.path.private}") String privateKeyPath,
            @Value("${jwt.path.public}") String publicKeyPath,
            @Value("${jwt.expriry}") Long expriry,
            UserRoleService userRoleService, UserService userService) {
        this.privateKeyPath = privateKeyPath;
        this.publicKeyPath = publicKeyPath;
        this.privateKey = getPrivateKeyFromFile();
        this.publicKey = getPublicKeyFromFile();
        this.userRoleService = userRoleService;
        this.expriry = expriry;
        this.userService = userService;
        this.rsaPublicKey = generatePublicKey(publicKey);
    }

    private String getPrivateKeyFromFile() {
        ClassPathResource resource = new ClassPathResource(privateKeyPath);
        try (InputStream is =  resource.getInputStream()) {
            String pem = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println(pem);
            return pem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replaceAll(System.lineSeparator(), "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .trim();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new InternalException(e.getMessage());
        }
    }

    private String getPublicKeyFromFile() {
        ClassPathResource resource = new ClassPathResource(publicKeyPath);
        try (InputStream is =  resource.getInputStream()) {
            String pem = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println(pem);
            return pem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replaceAll(System.lineSeparator(), "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .trim();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new InternalException(e.getMessage());
        }
    }

    private RSAPublicKey generatePublicKey(String publicKey) {
        try {
            String cleanKey = publicKey
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");

            byte[] keyBytes = Base64.getDecoder().decode(cleanKey);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) kf.generatePublic(spec);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new InternalException(e.getMessage());
        }
    }

    public String generateAccessToken(Users user) {
        List<String> userRoles = userRoleService.getAllRoleOfUser(user.getId()).stream()
                .map(Role::getCode).toList();

        try {
            byte[] encoded = Base64.getDecoder().decode(privateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = kf.generatePrivate(keySpec);

            JWSSigner signer = new RSASSASigner(privateKey);

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getFullName())
                    .issueTime(new Date())
                    .claim("sub", user.getId())
                    .claim("roles", userRoles)
                    .expirationTime(new Date(System.currentTimeMillis() + expriry))
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.RS256).build(),
                    claimsSet);

            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new InternalException(e.getMessage());
        }
    }

    public boolean validateToken(String token) {
        try {
            if (token == null || token.isEmpty()) return false;

            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new RSASSAVerifier(rsaPublicKey);

            // kiểm tra chữ ký
            if (!signedJWT.verify(verifier)) {
                return false;
            }

            // Kiểm tra thời hạn
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            return expirationTime != null && expirationTime.after(new Date());

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public CustomUserDetail parseUserFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            Map<String, Object> claims = signedJWT.getJWTClaimsSet().getClaims();
            Long userId = Long.valueOf(String.valueOf(claims.get("sub")));
            List<String> roles = (List) claims.get("roles");
            List<? extends GrantedAuthority> grantTypes = roles
                    .stream()
                    .filter(Objects::nonNull)
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .toList();

            Users users = userService.findById(userId).orElseThrow(
                    () -> new ResourceNotFoundException("User with id " + userId + "not found"));

            return new CustomUserDetail(
                    userId,
                    users.getFullName(),
                    users.getEmail(),
                    users.getPhoneNumber(),
                    grantTypes);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
