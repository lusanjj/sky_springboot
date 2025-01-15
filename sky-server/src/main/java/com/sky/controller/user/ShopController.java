package com.sky.controller.user;


import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: ShopController
 * Package:com.sky.controller.admin
 * Description:
 *
 * @Author Shane Liu
 * @Create 2025/1/15 16:01
 * @Version 1.0
 */
@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;

    public static final String KEY = "SHOP_STATUS";



    /**
     * 获取店铺的的营业状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺营业的状态")
    public Result<Integer> getStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取到店铺的印业状态为:{}",status == 1 ? "营业中":"已打烊");
        return Result.success(status);
    }
}
