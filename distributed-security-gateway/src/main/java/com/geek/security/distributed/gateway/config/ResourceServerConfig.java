package com.geek.security.distributed.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig {

    private static final String RESOURCE_ID = "res1";

    @Autowired
    private TokenStore tokenStore;

    public ResourceServerConfig() {
        super();
    }

    // uaa 资源。

    /**
     * 统一认证服务（UAA）资源拦截。
     */
    @Configuration
    @EnableResourceServer
    public class UAAServerConfig extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        super.configure(resources);
            resources
                    .resourceId(RESOURCE_ID)// 资源 id。
//                .tokenServices(tokenService())// 验证令牌的服务。

                    .tokenStore(tokenStore)
                    .stateless(true);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
            http
                    .authorizeRequests()
                    .antMatchers("/uaa/**").permitAll();
        }
    }

    // order 资源。

    /**
     * 统一用户服务。
     */
    @Configuration
    @EnableResourceServer
    public class OrderServerConfig extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        super.configure(resources);
            resources
                    .resourceId(RESOURCE_ID)// 资源 id。
//                .tokenServices(tokenService())// 验证令牌的服务。

                    .tokenStore(tokenStore)
                    .stateless(true);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
            http
                    .authorizeRequests()
                    .antMatchers("/order/**").access("#oauth2.hasScope('ROLE_API')");
        }

        // 配置其他资源服务。
    }
}
