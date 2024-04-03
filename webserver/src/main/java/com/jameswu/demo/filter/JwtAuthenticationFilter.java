package com.jameswu.demo.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.model.entity.UserProfile;
import com.jameswu.demo.service.CacheService;
import com.jameswu.demo.service.JwtService;
import com.jameswu.demo.utils.GzTexts;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    public JwtAuthenticationFilter(
            JwtService jwtService,
            CacheService cacheService,
            ObjectMapper objectMapper,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.jwtService = jwtService;
        this.cacheService = cacheService;
        this.objectMapper = objectMapper;
        this.resolver = resolver;
    }

    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final CacheService cacheService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getServletPath().contains("/api/v1/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!request.getServletPath().contains("/api/v1/public") && authHeader != null) {
            String accessToken = authHeader.replace(GzTexts.BEARER_PREFIX, "");
            try {
                long id = jwtService
                        .parsePayload(accessToken, JwtService.JWT_USER, UserProfile.class)
                        .getUserId();
                GcUser user = cacheService.retrieveOrLoadUser(id);
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ExpiredJwtException | SignatureException e) {
                resolver.resolveException(request, response, null, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
