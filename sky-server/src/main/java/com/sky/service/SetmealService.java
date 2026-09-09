package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import org.springframework.stereotype.Service;


public interface SetmealService {

    /*
    新增套餐
     */
    void save(SetmealDTO setmealDTO);

    /*
    套餐分页查询
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /*
    删除套餐
     */
    void deleteById(Long id);

    /*
    修改套餐
     */
    void update(SetmealDTO setmealDTO);

    /*
    套餐起售停售
     */
    void startOrStop(Integer status, Long id);
}
