package com.geek.security.distributed.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.geek.security.distributed.gateway.common.EncryptUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthFilter extends ZuulFilter {
    @Override
    public String filterType() {
//        return null;
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
        // 越小越优先。
    }

    @Override
    public boolean shouldFilter() {
//        return false;
        return true;
    }

    @Override
    public Object run() throws ZuulException {
//        return null;
        /**
         * 获取令牌内容。
         */
        RequestContext ctx = RequestContext.getCurrentContext();
        // 从安全上下文中拿到用户身份对象。
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof OAuth2Authentication)) {
            // 无 token 访问网关内资源的情况，目前仅有 uaa 服务直接暴露。
            return null;
        }

        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
        // 用户身份。
        String principal = userAuthentication.getName();
//        Object principal = userAuthentication.getPrincipal();

        /**
         * 组装明文 token，转发给微服务，放入 header，名称为 json-token。
         */
        List<String> authorities = new ArrayList<>();
        // 从 userAuthentication 取出权限，放在 authorities 中。
        userAuthentication.getAuthorities().stream().forEach(a -> authorities.add(a.getAuthority()));

        OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();
        Map<String, String> requestParameters = oAuth2Request.getRequestParameters();
        Map<String, Object> jsonToken = new HashMap<>(requestParameters);

        // 把身份信息和权限信息放在 json 中，加入 http 的 header 中。
        if (userAuthentication != null) {
            // 获取当前用户的身份信息。
            jsonToken.put("principal", principal);
            // 获取当前用户的权限信息。
            jsonToken.put("authorities", authorities);
        }

        // 把身份信息和权限信息放在 json 中，加入 http 的 header 中。
        // 转发给微服务。
        ctx.addZuulRequestHeader("json-token", EncryptUtil.encodeUTF8StringBase64(JSON.toJSONString(jsonToken)));

        return null;
    }
}
