package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

/**
 * ClassName: DishService
 * Package:com.sky.service
 * Description:
 *
 * @Author Shane Liu
 * @Create 2024-07-28 12:36 p.m.
 * @Version 1.0
 */
public interface DishService {
    /**
     * 新增菜品和对应的口味
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 菜品批量删除
     * @param ids
     */
    void deleteDishesBatch(List<Long> ids);
}
