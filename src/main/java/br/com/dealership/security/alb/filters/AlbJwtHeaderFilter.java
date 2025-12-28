package br.com.dealership.security.alb.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter that extracts JWT from AWS ALB header (x-amzn-oidc-data)
 * and converts it to standard Authorization Bearer header.
 *
 * This filter is only active when NOT in 'dev' profile.
 */
@Component
@Profile("!dev")
public class AlbJwtHeaderFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AlbJwtHeaderFilter.class);
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        
        String albJwt = request.getHeader("x-amzn-oidc-data");
        
        if (albJwt != null && !albJwt.isEmpty()) {
            HttpServletRequest wrappedRequest = new HttpServletRequestWrapper(request) {
                @Override
                public String getHeader(String name) {
                    if ("Authorization".equalsIgnoreCase(name)) {
                        return "Bearer " + albJwt;
                    }
                    return super.getHeader(name);
                }
            };
            filterChain.doFilter(wrappedRequest, response);
            return;
        }
        
        filterChain.doFilter(request, response);
    }
}