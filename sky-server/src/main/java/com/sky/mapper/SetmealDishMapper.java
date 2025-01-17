package com.sky.mapper;

import com.sky.entity.Dish;
import com.sky.entity.SetmealDish;
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

    /**
     * 保存套餐和菜品的关联关系
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 删除套餐餐品关系表中的数据
     * @param id
     */
    void deleteBySetmaleId(Long id);

    /**
     * 根据套餐信息查询菜品信息
     * @param id
     * @return
     */
    List<SetmealDish> getBySetmealId(Long id);
}
