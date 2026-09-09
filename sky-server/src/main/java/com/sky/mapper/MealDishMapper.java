package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MealDishMapper {

    List<Long> getMealToDishById(@Param("dishIds") List<Long>id);

}