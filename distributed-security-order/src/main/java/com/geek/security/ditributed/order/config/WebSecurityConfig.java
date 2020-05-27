package com.geek.security.ditributed.order.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // 安全拦截机制。（最重要）。
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //        super.configure(http);
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/r/**").authenticated()
                .anyRequest().permitAll();
    }
}
