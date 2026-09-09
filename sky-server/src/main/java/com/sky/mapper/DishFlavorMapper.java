package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /*
    加入菜品口味
     */
    void insert(@Param("flavors")List<DishFlavor> flavors);

    /*
    删除菜品口味
     */
    void deleteByDishIds(@Param("dishIdss")List<Long>ids);

    /*
    获取菜品口味
     */
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getById(Long id);
}
