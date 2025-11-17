package com.kmp.mephi.studsys.service;

import com.kmp.mephi.studsys.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtEncoder encoder;

    @Value("${app.jwt.access-ttl:PT15M}")
    private Duration accessTtl;

    @Value("${app.jwt.refresh-ttl:P7D}")
    private Duration refreshTtl;

    public String issueAccess(User user)  { return issue(user, accessTtl,  "access"); }
    public String issueRefresh(User user) { return issue(user, refreshTtl, "refresh"); }

    private String issue(User user, Duration ttl, String typ) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plus(ttl))
                .subject(user.getEmail())
                .claim("uid", user.getId())
                .claim("role", user.getRole().name())
                .claim("typ", typ)
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}

