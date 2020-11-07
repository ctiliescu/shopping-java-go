package com.ctiliescu.shopping.config;

import com.ctiliescu.shopping.config.filter.JWTAuthenticationFilter;
import com.ctiliescu.shopping.config.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthService authService;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        JWTAuthenticationFilter filter = new JWTAuthenticationFilter(authService);

        httpSecurity.csrf().disable().cors().disable().
                addFilterBefore(filter, JWTAuthenticationFilter.class).requestMatchers().antMatchers("/api/v1/**").and().authorizeRequests().anyRequest().authenticated();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/api/v1/users/login");
    }
}
