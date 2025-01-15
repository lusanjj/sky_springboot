package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.ResultType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: DishController
 * Package:com.sky.controller.admin
 * Description:
 *
 * @Author Shane Liu
 * @Create 2024-07-28 12:08 p.m.
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j

public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 启售停售菜品状态
     * @param status
     * @param id
     * @return
     */
    @PostMapping("status/{status}")
    @ApiOperation("启售和停售菜品状态")
    public Result enableOrDisable(@PathVariable Integer status, Long id){
        log.info("启售停售菜品状态：{}，{}",status,id);
        dishService.enableOrDisable(status,id);
        return Result.success();
    }

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品：{}",dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }


    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询：{}",dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 菜品分批删除
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("菜品删除")
    public Result deleteDishes(@RequestParam List<Long> ids){
        log.info("菜品删除：{}",ids);
        dishService.deleteDishesBatch(ids);
        return Result.success();
    }

    /**
     * 根据id查询菜品 ---修改回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据菜品id查询")
    public Result<DishVO> getDishById(@PathVariable Long id){
        log.info("根据菜品id查询:{}",id);
        DishVO dishVo = dishService.getDishWithFlavor(id);
        return Result.success(dishVo);
    }

    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品信息")
    public Result modifyDishes(@RequestBody DishDTO dishDTO){
        log.info("修改菜品，修改信息为：{}",dishDTO);
        dishService.modifyDishesWithFlavor(dishDTO);
        return Result.success();
    }











}
