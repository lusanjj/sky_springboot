package com.sky.mapper;

import com.sky.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * ClassName: SetmealDishMapper
 * Package:com.sky.mapper
 * Description:
 *
 * @Author Shane Liu
 * @Create 2024-07-30 5:07 p.m.
 * @Version 1.0
 */
@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品id查询对应的套餐id
     * @param dishIds
     * @return
     */
    List<Long> getSetmealIdsByDishId(List<Long> dishIds);
}
