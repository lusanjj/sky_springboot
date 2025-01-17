package com.sky.service;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

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
    void saveWithFlavor(DishDTO dishDTO);

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

    /**
     * 根据菜品id查询
     * @param id
     */
    DishVO getDishWithFlavor(Long id);

    /**
     * 根据菜品id修改菜品和口味信息
     * @param dishDTO
     */
    void modifyDishesWithFlavor(DishDTO dishDTO);

    /**
     * 启售停售菜品状态
     * @param status
     * @param id
     */
    void enableOrDisable(Integer status, Long id);


    List<Dish> list(Long categoryId);

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
