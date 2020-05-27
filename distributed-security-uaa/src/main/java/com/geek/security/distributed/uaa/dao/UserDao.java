package com.geek.security.distributed.uaa.dao;

import com.geek.security.distributed.uaa.model.PermissionDto;
import com.geek.security.distributed.uaa.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据用户 id 查询用户权限。
     *
     * @param userId
     * @return
     */
    public List<String> findPermissionsByUserId(String userId) {
        String sql = "SELECT \n" +
                "    *\n" +
                "FROM\n" +
                "    t_permission\n" +
                "WHERE\n" +
                "    id IN (SELECT \n" +
                "            permission_id\n" +
                "        FROM\n" +
                "            t_role_permission\n" +
                "        WHERE\n" +
                "            role_id IN (SELECT \n" +
                "                    role_id\n" +
                "                FROM\n" +
                "                    t_user_role\n" +
                "                WHERE\n" +
                "                    user_id = ?));";
        List<PermissionDto> list = jdbcTemplate.query(sql, new Object[]{userId}, new BeanPropertyRowMapper<>(PermissionDto.class));
        List<String> permissionList = new ArrayList<>();
        list.forEach(c -> permissionList.add(c.getCode()));
        return permissionList;
    }

    /**
     * 根据账号查询用户信息。
     *
     * @param username
     * @return
     */
    public UserDto getUserByUsername(String username) {
        String sql = "select id, username, password, fullname, mobile from t_user where username = ?";
        List<UserDto> userDtoList = jdbcTemplate.query(sql, new Object[]{username}, new BeanPropertyRowMapper<>(UserDto.class));
        if (userDtoList != null && userDtoList.size() == 1) {
            return userDtoList.get(0);
        }
        return null;
    }
}
