package com.geek.security.distributed.uaa.service;

import com.geek.security.distributed.uaa.dao.UserDao;
import com.geek.security.distributed.uaa.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpringDataUserDetailService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    // 根据账号查询用户信息。
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 将来连接数据库根据账号查询用户信息。
        UserDto userDto = userDao.getUserByUsername(username);
        if (userDto == null) {
            // 如果用户查不到，返回 null，由 provider 来抛出异常。
            return null;
        }

        // 根据用户 id 查询用户的权限。
        List<String> permissions = userDao.findPermissionsByUserId(userDto.getId());
        // 将 permissions 转为数组。
        String[] permissionArray = new String[permissions.size()];
        permissions.toArray(permissionArray);

        UserDetails userDetails = User.withUsername(userDto.getUsername()).password(userDto.getPassword()).authorities(permissionArray).build();


        // 登录账号。
        System.out.println("username = " + username);
        // 暂时采用模拟方式。
//        UserDetails userDetails = User.withUsername("zhangsan").password("$2a$10$ZV2gne5gM44.eRTi0KFOK.MJ/OmjB3h6Aw1sUk9YoOU8rbSUHtYwG").authorities("p1").build();

//        UserDetails userDetails = User.withUsername(userDto.getUsername()).password(userDto.getPassword()).authorities("p1").build();

        return userDetails;
    }
}
