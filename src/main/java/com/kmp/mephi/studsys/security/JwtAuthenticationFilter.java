package com.kmp.mephi.studsys.security;


import com.kmp.mephi.studsys.entity.User;
import com.kmp.mephi.studsys.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;
    private final UserRepository userRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {
        String auth = req.getHeader("Authorization");

        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            try {
                Jwt jwt = jwtDecoder.decode(token);

                String typ = jwt.getClaimAsString("typ");
                if (!"access".equals(typ)) {
                    chain.doFilter(req, res);
                    return;
                }

                String email = jwt.getSubject();
                userRepo.findByEmail(email).ifPresent(this::setAuth);
            } catch (JwtException ignored) {

            }
        }

        chain.doFilter(req, res);
    }

    private void setAuth(User user) {
        var authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
        var authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(), null, List.of(authority)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String p = request.getServletPath();
        return p.startsWith("/api/auth")
                || p.startsWith("/swagger-ui")
                || p.startsWith("/v3/api-docs")
                || p.equals("/error");
    }
}
