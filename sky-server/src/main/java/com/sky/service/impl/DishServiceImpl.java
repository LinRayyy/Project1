package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.MealDishMapper;
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

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private MealDishMapper MealDishMapper;

    public void saveWithDish(DishDTO dishDTO){
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.insert(dish);
        //获取Id插入口味
        Long dishId=dish.getId();
        List<DishFlavor> flavors=dishDTO.getFlavors();
        if(flavors!=null&&flavors.size()>0){
            for(DishFlavor dishFlavor:flavors){
                dishFlavor.setDishId(dishId);
            }
        }
        dishFlavorMapper.insert(flavors);
    }

    /*
    菜品分页查询
     */
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO){
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO>page=dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /*
    批量删除菜品
     */
    @Transactional
    public void delectById(List<Long> ids){
        //判断菜品是否在售卖，如果在，不能删除
        for(Long id:ids){
            Dish dish=dishMapper.getById(id);
            if(dish==null||dish.getStatus().equals(StatusConstant.DISABLE)){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //判断菜品是否在套餐中
        List<Long> dishById = MealDishMapper.getMealToDishById(ids);
        if(dishById!=null&&dishById.size()>0)
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        //删除菜品表中的菜品数据
        dishMapper.deleteByIds(ids);
        //删除菜品关联的口味数据
        dishFlavorMapper.deleteByDishIds(ids);

    }

    /*
   根据id获取菜品
    */
    public DishVO getById(Long id){
        Dish dish = dishMapper.getById(id);
        List<DishFlavor> dishFlavor = dishFlavorMapper.getById(id);
        DishVO dishVO=new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavor);
        return dishVO;
    }

    /*
    根据id修改菜品
     */
    public void updateDish( DishDTO dishDTO){
        //修改基本菜品信息
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);
        //删除原有口味
        List<Long> dishId=new ArrayList<>();
        dishId.add(dish.getId());
        dishFlavorMapper.deleteByDishIds(dishId);
        //重新加入口味
        List<DishFlavor> flavors=dishDTO.getFlavors();
        if(flavors!=null&&flavors.size()>0){
            for(DishFlavor dishFlavor:flavors){
                dishFlavor.setDishId(dish.getId());
            }
            dishFlavorMapper.insert(flavors);
        }


    }
}






























