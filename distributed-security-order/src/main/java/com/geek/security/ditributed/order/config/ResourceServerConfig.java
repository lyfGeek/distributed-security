package com.geek.security.ditributed.order.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "res1";

    @Autowired
    private TokenStore tokenStore;

    public ResourceServerConfig() {
        super();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        super.configure(resources);
        resources
                .resourceId(RESOURCE_ID)// 资源 id。
//                .tokenServices(tokenService())// 验证令牌的服务。

                // ~~~
                .tokenStore(tokenStore)
                // ~~~

                .stateless(true);
    }

    // 资源服务令牌解析服务。
/*    @Bean
    public ResourceServerTokenServices tokenService() {
        // 使用远程服务请求授权服务器校验 token 的 url、client_id、client_secret。
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        remoteTokenServices.setCheckTokenEndpointUrl("http://localhost:53020/uaa/oauth/check_token");
        remoteTokenServices.setClientId("c1");
        remoteTokenServices.setClientSecret("secret");
        return remoteTokenServices;
    }*/

    @Override
    public void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
        http.
                authorizeRequests()
                .antMatchers("/**").access("#oauth2.hasScope('ROLE_ADMIN')")
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);// 基于 token，不再记录 session。
    }
}
