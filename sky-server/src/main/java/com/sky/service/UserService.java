package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * ClassName: UserService
 * Package:com.sky.service
 * Description:
 *
 * @Author Shane Liu
 * @Create 2025/1/16 21:34
 * @Version 1.0
 */
public interface UserService {

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    User wxLogin(UserLoginDTO userLoginDTO);
}
