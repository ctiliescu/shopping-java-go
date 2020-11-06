package com.ctiliescu.shopping.config.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    private final static Logger LOG = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        LOG.info("Logging Request  {} : {}", req.getMethod(), req.getRequestURI());
        Authentication authentication = new PreAuthenticatedAuthenticationToken(null, null, null);
        successfulAuthentication((HttpServletRequest) request, (HttpServletResponse) response, authentication);
        chain.doFilter(request, response);
        LOG.info("Logging Response :{}", res.getContentType());
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return "N/A";
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "N/A";
    }

    @Override
    public void destroy() {
        LOG.warn("Destructing filter :{}", this);
    }
}
