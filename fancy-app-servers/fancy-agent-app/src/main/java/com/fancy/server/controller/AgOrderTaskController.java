package com.fancy.server.controller;


import com.fancy.common.pojo.PageResult;
import com.fancy.module.common.repository.dto.OrderTaskListDTO;
import com.fancy.module.common.repository.pojo.AgOrderTask;
import com.fancy.module.common.repository.vo.OrderTaskListVO;
import com.fancy.module.common.service.AgOrderTaskService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单任务表 前端控制器
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@RestController
@RequestMapping("/agOrderTask")
public class AgOrderTaskController {

    @Resource
    private AgOrderTaskService agOrderTaskService;

    @PostMapping("/listOrderTask")
    public PageResult<OrderTaskListVO> listOrderTask(@RequestBody OrderTaskListDTO orderTaskListDTO) {
        return agOrderTaskService.listOrderTask(orderTaskListDTO);
    }

}
