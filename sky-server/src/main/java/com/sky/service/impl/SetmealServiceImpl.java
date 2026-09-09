package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private DishMapper dishMapper;


    /*
    新增套餐
     */
    @Transactional
    public void save(SetmealDTO setmealDTO){
        //获取setmeal对象，并将其放入setmeal表里
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.insert(setmeal);
        Long id = setmeal.getId();
        //获取里面的List<SetmealDish>，将这些放入setmeal_dish表里
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes != null && !setmealDishes.isEmpty()){
            for(SetmealDish setmealDish : setmealDishes){
                setmealDish.setSetmealId(id);
            }
            setmealDishMapper.insert(setmealDishes);
        }
    }

    /*
  套餐分页查询
   */
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO){
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO>page=setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /*
    删除套餐
     */
    @Transactional
    public void deleteById(Long id){
        //先删除套餐关联的菜品关系数据（中间表setmeal_dish）
        setmealDishMapper.deleteBySetmealId(id);
        //再删除套餐本身
        setmealMapper.deleteById(id);
    }

    /*
    修改套餐
     */
    @Transactional
    public void update(SetmealDTO setmealDTO){
        //1.修改套餐基本信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);

        //2.先删除该套餐原有的菜品关联数据（中间表setmeal_dish）
        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());

        //3.重新建立套餐和菜品的关联关系
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes != null && !setmealDishes.isEmpty()){
            for(SetmealDish setmealDish : setmealDishes){
                setmealDish.setSetmealId(setmealDTO.getId());
            }
            setmealDishMapper.insert(setmealDishes);
        }
    }

    /*
    套餐起售停售
     */
    public void startOrStop(Integer status, Long id){
        //1.起售校验：套餐内若存在停售的菜品，则不允许起售
        if(status == StatusConstant.ENABLE){
            List<Dish> dishList = dishMapper.getBySetmealId(id);
            if(dishList != null && !dishList.isEmpty()){
                for(Dish dish : dishList){
                    if(StatusConstant.DISABLE.equals(dish.getStatus())){
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                }
            }
        }

        //2.更新套餐的起售状态
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }

}
