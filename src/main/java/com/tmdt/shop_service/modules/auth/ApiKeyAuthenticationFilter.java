package com.tmdt.shop_service.modules.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {
    final static String API_KEY_HEADER = "X-API-KEY";
    final String validApiKeyAdmin;
    final String validApiKeyUser;
    public ApiKeyAuthenticationFilter() {
        this.validApiKeyAdmin = "api-key-admin";
        this.validApiKeyUser = "api-key-user";
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String apiKey = request.getHeader(API_KEY_HEADER);
        if (validApiKeyAdmin.equals(apiKey)) {
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            UserDetails userDetails = new CustomUserDetail(
                    1L,
                    "Mai Văn Hiền",
                    "maivanhien244@gmail.com",
                    "0862990700",
                    grantedAuthorities);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(token);
        } else if  (validApiKeyUser.equals(apiKey)) {
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
            UserDetails userDetails = new CustomUserDetail(
                    2L,
                    "Mai Văn Hiền CUSTOMER",
                    "maivanhien155@gmail.com",
                    "0392407024",
                    grantedAuthorities);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(request, response);
    }
}
