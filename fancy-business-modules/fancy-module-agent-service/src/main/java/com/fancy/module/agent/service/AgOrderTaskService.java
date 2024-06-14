package com.fancy.module.agent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fancy.common.pojo.PageResult;
import com.fancy.module.agent.api.task.dto.OrderTaskListDTO;
import com.fancy.module.agent.controller.req.AgOrderTaskImportReq;
import com.fancy.module.agent.controller.vo.AgUploadTaskReqVO;
import com.fancy.module.agent.repository.pojo.AgOrderTask;
import com.fancy.module.agent.controller.vo.OrderTaskListVO;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 订单任务表 服务类
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
public interface AgOrderTaskService extends IService<AgOrderTask> {

    PageResult<OrderTaskListVO> listOrderTask(OrderTaskListDTO orderTaskListDTO);

    @Transactional(rollbackFor = Exception.class)
    List<AgUploadTaskReqVO> csvUploadTask(List<AgOrderTaskImportReq> agOrderTaskImportReqs);
}
