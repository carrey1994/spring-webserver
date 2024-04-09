package com.jameswu.demo.filter;

import com.jameswu.demo.exception.GcException.TokenInvalidException;
import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.service.CacheService;
import com.jameswu.demo.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Qualifier("handlerExceptionResolver") private HandlerExceptionResolver resolver;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private CacheService cacheService;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (request.getServletPath().contains("/api/v1/login")) {
			filterChain.doFilter(request, response);
			return;
		}

		String accessToken = jwtService.trimBearerToken(request);
		if (!request.getServletPath().contains("/api/v1/public") && accessToken != null) {
			try {
				// todo: check if token exists first.
				jwtService.throwIfTokenNotFound(accessToken, new TokenInvalidException());
				int id = jwtService.parseUserProfile(accessToken).getUserId();
				GcUser user = cacheService.retrieveOrLoadUser(id);
				var authToken =
						new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authToken);
			} catch (JwtException e) {
				resolver.resolveException(request, response, null, e);
				return;
			}
		}
		filterChain.doFilter(request, response);
	}
}
