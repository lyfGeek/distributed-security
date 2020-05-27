package com.geek.security.distributed.uaa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private AuthenticationManager authenticationManager;

    // ~~~
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    // ~~~
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthorizationServer() {
        super();
    }

    // 设置授权码模式的授权码如果存取。
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }

    // 令牌管理服务。
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setClientDetailsService(clientDetailsService);// 客户端详情服务。
        services.setSupportRefreshToken(true);// 支持刷新令牌。
        services.setTokenStore(tokenStore);// 令牌存储策略。

        // ~~~令牌增强。
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter));
        services.setTokenEnhancer(tokenEnhancerChain);
        // ~~~

        services.setAccessTokenValiditySeconds(7200);// 令牌默认有效期 2 小时。
        services.setRefreshTokenValiditySeconds(259200);// 刷新令牌默认有效期 3 天。
        return services;
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        return new JdbcAuthorizationCodeServices(dataSource);// 设置授权码模式的授权码如何存取。
    }

    /**
     * + AuthorizationServerSecurityConfigurer security。
     * 配置令牌端点的安全约束。
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        super.configure(security);
        security
                .tokenKeyAccess("permitAll()")// oauth/token_key 公开。
                .checkTokenAccess("permitAll()")// oauth/check_token 公开。
                .allowFormAuthenticationForClients();// 表单认证。（申请令牌）。
    }

    @Bean
    public ClientDetailsService clientDetailsService(DataSource dataSource) {
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);
        return jdbcClientDetailsService;
    }

    /**
     * + ClientDetailsServiceConfigurer。
     * 配置客户端详情服务（ClientDetailService）。客户端详情信息在这里初始化。
     * 客户端详情信息直接写死在这里或通过数据库存储调取详情信息。
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        super.configure(clients);

        clients
                .withClientDetails(clientDetailsService);

//        clients.inMemory()// 使用 in-memory 存储。
//                .withClient("c1")// clientId
//                .secret(new BCryptPasswordEncoder().encode("secret"))// 客户端密钥。
//                .resourceIds("res1")
//                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")// 该 client 允许的授权类型。
//                .scopes("all")// 允许的授权范围。
//                .autoApprove(false)// false 跳转到授权页面。
//                // 验证回调地址。
//                .redirectUris("http://www.baidu.com");
    }

    /**
     * + AuthorizationServerEndpointsConfigurer endpoints。
     * 用来配置令牌（token）的访问端点和令牌服务（token services）。
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        super.configure(endpoints);
        endpoints
                .authenticationManager(authenticationManager)// 认证管理器。
                .authorizationCodeServices(authorizationCodeServices)// 授权码码服务。
                .tokenServices(tokenServices())// 令牌管理服务。
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }
}
