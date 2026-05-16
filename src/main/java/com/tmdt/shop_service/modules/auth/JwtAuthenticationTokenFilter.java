package com.tmdt.shop_service.modules.auth;

import com.nimbusds.jwt.SignedJWT;
import com.tmdt.shop_service.core.exception.ForbiddenException;
import com.tmdt.shop_service.core.exception.UnAuthorizationException;
import com.tmdt.shop_service.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    final JwtUtils jwtUtils;

    public List<String> permitUrls = List.of(
            "/v3/api-docs.yaml",
            "/v3/api-docs",
            "/swagger-ui",
            "/swagger-ui.html",
            "/v1/auth",
            "/v1/upload",
            "/v1/public");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);
        if (token == null || token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean validateToken = jwtUtils.validateToken(token);

        if (!validateToken) {
            String uri = request.getRequestURI();
            boolean anyMatch = permitUrls.stream().anyMatch(uri::startsWith);
            if (anyMatch) {
                filterChain.doFilter(request, response);
                return;
            }
            throw new ForbiddenException("Token is not valid");
        }

        CustomUserDetail customUserDetail = jwtUtils.parseUserFromToken(token);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                customUserDetail, null, customUserDetail.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        if  (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}
