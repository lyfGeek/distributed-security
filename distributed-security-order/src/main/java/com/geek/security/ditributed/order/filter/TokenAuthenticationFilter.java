package com.geek.security.ditributed.order.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geek.security.ditributed.order.common.EncryptUtil;
import com.geek.security.ditributed.order.model.UserDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        // 解析出头中的 token。
        String token = httpServletRequest.getHeader("json-token");
        if (token != null) {
            // 解析 token。
            String json = EncryptUtil.decodeUTF8StringBase64(token);
            // 将 token 转成 json 对象。
            JSONObject jsonObject = JSON.parseObject(json);
            // 用户身份信息。
            UserDto userDto = new UserDto();
            String principal = jsonObject.getString("principal");
            userDto.setUsername(principal);
            // 用户权限。
            JSONArray authoritiesArray = jsonObject.getJSONArray("authorities");
            String[] authorities = authoritiesArray.toArray(new String[authoritiesArray.size()]);
            // 将用户信息和权限填充到用户身份 token 对象中。
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDto, null, AuthorityUtils.createAuthorityList(authorities));
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            // 将 authentication 保存到安全上下文。
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
