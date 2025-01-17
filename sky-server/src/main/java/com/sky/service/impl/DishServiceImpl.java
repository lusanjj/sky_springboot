package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Employee;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: DishServiceImpl
 * Package:com.sky.service.impl
 * Description:
 *
 * @Author Shane Liu
 * @Create 2024-07-28 12:40 p.m.
 * @Version 1.0
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;


     /** 新增菜品和对应的口味
     * @param dishDTO
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        //向菜品表插入1条数据
        dishMapper.insert(dish);
        //获取主键值，从sql语句执行完insert语句之后，会保存主键值给名为id
        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });


        //向口味表插入n 条数据,当dishDTO中的flavors有值，插入表中才有意义
            //批量插入口味数据到mapper中，再进入到口味表
            dishFlavorMapper.insertBatch(flavors);

        }
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 菜品批量删除操作
     * @param ids
     */
    @Transactional
    @Override
    public void deleteDishesBatch(List<Long> ids) {
        //判断当前菜品能否可以被删除 ----起售中的菜品不能删除 dish.status ！= 1
        for (Long id : ids) {
            Dish dish = dishMapper.getByDishId(id);
            //判断菜品是有没有在售卖，如果状态是在售卖，那么抛出异常
            if(dish.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //判断当前菜品能否可以被删除 ----被套餐关联的菜品不能删除
            //判断菜品是不是包含在套餐里，如果状态是在售卖，那么抛出异常
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishId(ids);
            //判断是否通过菜名id查取到有相关的套餐包含了
        if(setmealIds != null && setmealIds.size() > 0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }


//        //可以一次删除一个菜品，也可以批量删除
//        for (Long id : ids) {
//            dishMapper.deleteByDishId(id);
//            //删除菜品后，关联的口味数据也需要删除掉
//            dishFlavorMapper.deleteFlavorByDishId(id);
//        }

        //删除菜品表中的菜品数据
        //sql: delete from dish where id in (?,?,?)
        dishMapper.deleteByIds(ids);

        //根据菜品id集合批量删除菜品数据
        //sql: delete from dish_flavor where dish_id in (?,?,?)
        dishFlavorMapper.deleteFlavorByDishIds(ids);

    }

    /**
     * 根据菜品id查询
     * @param id
     */
    @Override
    public DishVO getDishWithFlavor(Long id) {
        //根据id查询菜品数据
        Dish dish = dishMapper.getByDishId(id);

        //将查询到的数据封装到VO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);

        //根据id 查询菜品口味数据
        dishVO.setFlavors(dishFlavorMapper.getFlavorByDishId(id));
        return dishVO;

    }

    /**
     * 根据菜品id修改菜品和口味信息
     * @param dishDTO
     */
    @Override
    public void modifyDishesWithFlavor(DishDTO dishDTO) {
        //创建新的dish 对象
        Dish dish = new Dish();
        //把用户通过前端传来的数据传入新dish对象中从而达到更改
        BeanUtils.copyProperties(dishDTO,dish);
        //把新的菜品信息传递给dish对象后，通过dishMapper 实现数据库更改
        dishMapper.modifyDishes(dish);


        //删除原有的口味数据
        dishFlavorMapper.deleteFlavorByDishId(dishDTO.getId());

        //插入新的口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });


            //向口味表插入n 条数据,当dishDTO中的flavors有值，插入表中才有意义
            //批量插入口味数据到mapper中，再进入到口味表
            dishFlavorMapper.insertBatch(flavors);

        }

    }

    /**
     * 启售停售菜品状态
     * @param status
     * @param id
     */
    @Override
    public void enableOrDisable(Integer status, Long id) {
        Dish dish = Dish.builder()
                .status(status)
                .id(id)
                .build();
        dishMapper.modifyDishes(dish);
    }


    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    @Override
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);
        List<DishVO> dishVOList = new ArrayList<>();

        for(Dish d : dishList){
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

//            根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getFlavorByDishId(d.getId());


            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }


}

























