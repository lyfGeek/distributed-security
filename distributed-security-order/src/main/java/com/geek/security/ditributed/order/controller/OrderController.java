package com.geek.security.ditributed.order.controller;

import com.geek.security.ditributed.order.model.UserDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @GetMapping("/r1")
    @PreAuthorize("hasAnyAuthority('p1')")
    public String r1() {
        // 获取用户的身份信息。
        UserDto userDto = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDto.getUsername() + "访问资源 1。";
    }
}
