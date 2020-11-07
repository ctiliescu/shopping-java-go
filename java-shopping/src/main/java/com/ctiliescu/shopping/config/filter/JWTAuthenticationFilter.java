package com.ctiliescu.shopping.config.filter;

import com.ctiliescu.shopping.config.model.Credential;
import com.ctiliescu.shopping.config.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JWTAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {
    private final static Logger LOG = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
    private AuthService authService;

    public JWTAuthenticationFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        Optional<Credential> credentialOptional = authService.checkToken(req.getHeader("Authorized"));
        if(credentialOptional.isPresent()) {
            Authentication authentication = new PreAuthenticatedAuthenticationToken(credentialOptional.get().getUserId(), credentialOptional.get().getJwdId(), null);
            successfulAuthentication((HttpServletRequest) request, (HttpServletResponse) response, authentication);
            chain.doFilter(request, response);
            LOG.info("Logging Response :{}", res.getContentType());
        } else {
            res.setStatus(401);
            return;
        }
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
