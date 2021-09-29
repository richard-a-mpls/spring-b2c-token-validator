package com.rca.aurhotization.azureb2ctokenvalidator.filters;

import com.rca.aurhotization.azureb2ctokenvalidator.utilities.Constants;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Service
public class AuthorizeRequestFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String key = System.getenv(Constants.ENV_KEY);
        String secret = System.getenv(Constants.ENV_SECRET);

        if (key == null && secret == null) {
            // security not configured
            SecurityContextHolder.getContext().setAuthentication(
                    getAuthentication("security", "not_configured"));
        } else {
            // security configured
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            String securityHeaderSecret = req.getHeader(key);
            if (secret.equals(securityHeaderSecret)) {
                SecurityContextHolder.getContext().setAuthentication(getAuthentication(key, secret));
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String key, String secret) {
        return new UsernamePasswordAuthenticationToken(key, secret, null);
    }
}
