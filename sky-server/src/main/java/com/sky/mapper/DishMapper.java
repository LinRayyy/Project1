package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishMapper {



    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /*
    添加菜品
     */
    @AutoFill(value = OperationType.INSERT)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into dish (name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) " +
            "values (#{name}, #{categoryId}, #{price}, #{image}, #{description}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Dish dish);



    /*
    菜品分页查询
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /*
    根据id查找菜品
     */
    @Select("SELECT * from dish where id=#{id}")
     Dish getById(Long id);

    /*
    批量删除菜品
     */
    void deleteByIds(List<Long> ids);

    /*
    加入菜品
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    /*
    根据套餐id查询套餐下的菜品（起售校验用）
     */
    List<Dish> getBySetmealId(Long setmealId);
}
