package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /*
    批量添加套餐菜品关系
     */
    void insert(@Param("setmealDishes") List<SetmealDish> setmealDishes);

    /*
    根据套餐id批量删除套餐菜品关系
     */
    void deleteBySetmealId(Long setmealId);
}
