package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * ClassName: UserMapper
 * Package:com.sky.mapper
 * Description:
 *
 * @Author Shane Liu
 * @Create 2025/1/16 22:37
 * @Version 1.0
 */

@Mapper
public interface UserMapper {
    /**
     * 根据openid查询用户
     * @param openid
     * @return
     */
    @Select("select * from user where openid = #{openid}")
    User getByOpenId(String openid);


    /**
     * 插入用户数据
     * @param user
     */
    void insert(User user);
}
