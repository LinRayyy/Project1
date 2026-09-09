package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags="套餐相关接口")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /*
    新增套餐
     */
    @PostMapping
    @ApiOperation("新增套餐接口")
    public Result<?> save(@RequestBody SetmealDTO setmealDTO) {
        log.info("开始新增套餐，{}", setmealDTO);
        setmealService.save(setmealDTO);
        return Result.success();
    }

    /*
    套餐分页查询
     */
    @GetMapping("/page")
    @ApiOperation("套餐分页查询接口")
    public Result<PageResult>pageQuery( SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("开始套餐分页查询");
        PageResult page=setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(page);
    }

    /*
    删除套餐
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除套餐接口")
    public Result<?> deleteById(@PathVariable Long id) {
        log.info("开始删除套餐，id：{}", id);
        setmealService.deleteById(id);
        return Result.success();
    }

    /*
    修改套餐
     */
    @PutMapping
    @ApiOperation("修改套餐接口")
    public Result<?> update(@RequestBody SetmealDTO setmealDTO) {
        log.info("开始修改套餐，{}", setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }

    /*
    套餐起售停售
     */
    @PostMapping("/status/{status}")
    @ApiOperation("套餐起售停售接口")
    public Result<?> startOrStop(@PathVariable Integer status, @RequestParam Long id) {
        log.info("套餐起售停售，status：{}，id：{}", status, id);
        setmealService.startOrStop(status, id);
        return Result.success();
    }

}
