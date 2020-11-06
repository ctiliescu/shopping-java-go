package com.ctiliescu.shopping.config;

import com.ctiliescu.shopping.config.filter.JWTAuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        JWTAuthenticationFilter filter = new JWTAuthenticationFilter();

        httpSecurity.
                antMatcher("/api/v1/**").
                addFilter(filter).authorizeRequests().anyRequest().authenticated().and();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/api/v1/users/login");
    }
}
