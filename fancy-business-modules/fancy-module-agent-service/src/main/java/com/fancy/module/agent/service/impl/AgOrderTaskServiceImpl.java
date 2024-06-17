package com.fancy.module.agent.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.common.pojo.PageResult;
import com.fancy.module.agent.api.task.dto.OrderTaskListDTO;
import com.fancy.module.agent.controller.req.AgOrderTaskImportReq;
import com.fancy.module.agent.controller.vo.AgUploadTaskReqVO;
import com.fancy.module.agent.controller.vo.AgUploadTaskReqVO.AgUploadTaskErrorVo;
import com.fancy.module.agent.enums.TaskStatusEnum;
import com.fancy.module.agent.repository.mapper.AgOrderTaskMapper;
import com.fancy.module.agent.repository.pojo.AgContentServiceMain;
import com.fancy.module.agent.repository.pojo.AgMerchant;
import com.fancy.module.agent.repository.pojo.AgMerchantOrder;
import com.fancy.module.agent.repository.pojo.AgMerchantOrderDetail;
import com.fancy.module.agent.repository.pojo.AgOrderFlow;
import com.fancy.module.agent.repository.pojo.AgOrderTask;
import com.fancy.module.agent.controller.vo.OrderTaskListVO;
import com.fancy.module.agent.service.AgContentServiceMainService;
import com.fancy.module.agent.service.AgMerchantOrderDetailService;
import com.fancy.module.agent.service.AgMerchantOrderService;
import com.fancy.module.agent.service.AgMerchantService;
import com.fancy.module.agent.service.AgOrderFlowService;
import com.fancy.module.agent.service.AgOrderTaskService;
import com.fancy.module.common.api.user.UserApi;
import com.fancy.module.common.api.user.dto.UserRespDTO;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 订单任务表 服务实现类
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@Service
public class AgOrderTaskServiceImpl extends ServiceImpl<AgOrderTaskMapper, AgOrderTask> implements AgOrderTaskService {

    @Resource
    private AgContentServiceMainService agContentServiceMainService;

    @Resource
    private AgOrderTaskMapper agOrderTaskMapper;

    @Resource
    private AgMerchantService agMerchantService;

    @Resource
    private UserApi userApi;

    @Resource
    private AgOrderFlowService agOrderFlowService;

    @Resource
    private AgMerchantOrderService agMerchantOrderService;

    @Resource
    private AgMerchantOrderDetailService agMerchantOrderDetailService;


    @Override
    public PageResult<OrderTaskListVO> listOrderTask(OrderTaskListDTO orderTaskListDTO) {
        String contentName = orderTaskListDTO.getContentName();
        Integer contentType = orderTaskListDTO.getContentType();
        List<AgContentServiceMain> serviceMainList = new ArrayList<>();
        if (StringUtils.isNotBlank(contentName) || contentType != null) {
            // 查询内容id
            serviceMainList = agContentServiceMainService.list(Wrappers.lambdaQuery(AgContentServiceMain.class)
                    .eq(contentType != null, AgContentServiceMain::getContentType, contentType)
                    .like(StringUtils.isNotBlank(contentName), AgContentServiceMain::getContentName, contentName)
            );
        }

        // 分页查询
        PageResult<AgOrderTask> agOrderTaskPageResult = agOrderTaskMapper.selectPage(orderTaskListDTO, Wrappers.lambdaQuery(AgOrderTask.class)
                .eq(Objects.nonNull(orderTaskListDTO.getTaskStatus()), AgOrderTask::getTaskStatus, orderTaskListDTO.getTaskStatus())
                .in(CollectionUtil.isNotEmpty(serviceMainList), AgOrderTask::getContentId, serviceMainList.stream().map(AgContentServiceMain::getId).toList())
                .eq(Objects.nonNull(orderTaskListDTO.getMerchantId()), AgOrderTask::getAgMerchantId, orderTaskListDTO.getMerchantId())
                .in(CollectionUtil.isNotEmpty(orderTaskListDTO.getFancyItemId()), AgOrderTask::getFancyItemId, orderTaskListDTO.getFancyItemId())
                .like(StringUtils.isNotBlank(orderTaskListDTO.getFancyItemName()), AgOrderTask::getFancyItemName, orderTaskListDTO.getFancyItemName())
                .ge(Objects.nonNull(orderTaskListDTO.getTaskCreateTime()), AgOrderTask::getTaskCreateTime, orderTaskListDTO.getTaskCreateTime())
                .le(Objects.nonNull(orderTaskListDTO.getTaskFinishTime()), AgOrderTask::getTaskCreateTime, orderTaskListDTO.getTaskCreateTime())
                .orderByDesc(AgOrderTask::getTaskCreateTime)
        );

        // 查询内容相关信息
        if (CollectionUtil.isNotEmpty(agOrderTaskPageResult.getList())) {
            serviceMainList = agContentServiceMainService.list(Wrappers.lambdaQuery(AgContentServiceMain.class)
                    .in(AgContentServiceMain::getId, agOrderTaskPageResult.getList().stream().map(AgOrderTask::getContentId).toList())
            );
        }

        // 转换
        List<OrderTaskListVO> orderTaskListVOS = new ArrayList<>();
        for (AgOrderTask agOrderTask : agOrderTaskPageResult.getList()) {
            OrderTaskListVO orderTaskListVO = new OrderTaskListVO();
            BeanUtil.copyProperties(agOrderTask, orderTaskListVO);
            for (AgContentServiceMain agContentServiceMain : serviceMainList) {
                if (agOrderTask.getContentId().equals(agContentServiceMain.getId())) {
                    orderTaskListVO.setContentName(agContentServiceMain.getContentName());
                    orderTaskListVO.setContentType(agContentServiceMain.getContentType());
                }
            }
            orderTaskListVOS.add(orderTaskListVO);
        }
        PageResult<OrderTaskListVO> orderTaskListVOPageResult = new PageResult<>(orderTaskListDTO.getPageNum(), orderTaskListDTO.getPageSize());
        BeanUtil.copyProperties(agOrderTaskPageResult, orderTaskListVOPageResult);
        orderTaskListVOPageResult.setList(orderTaskListVOS);
        return orderTaskListVOPageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AgUploadTaskReqVO csvUploadTask(List<AgOrderTaskImportReq> agOrderTaskImportReqs) {
        int successCount = 0;
        AgUploadTaskReqVO agUploadTaskReqVO = new AgUploadTaskReqVO();
        List<AgUploadTaskErrorVo> agUploadTaskErrorVos = new ArrayList<>();
        for (AgOrderTaskImportReq agOrderTaskImportReq : agOrderTaskImportReqs) {
            AgUploadTaskErrorVo agUploadTaskErrorVo = new AgUploadTaskErrorVo();
            AgOrderTask agOrderTask = new AgOrderTask();
            BeanUtil.copyProperties(agOrderTaskImportReq,agOrderTask, "taskStatus");
            // 任务状态转换
            TaskStatusEnum taskStatusEnum = TaskStatusEnum.getByName(agOrderTaskImportReq.getTaskStatus());
            if (taskStatusEnum == null) {
                agUploadTaskErrorVo.setContentId(agOrderTaskImportReq.getContentId());
                agUploadTaskErrorVo.setFancyItemId(agOrderTaskImportReq.getFancyItemId());
                agUploadTaskErrorVo.setErrorMsg("任务状态不存在");
                agUploadTaskErrorVos.add(agUploadTaskErrorVo);
                continue;
            }
            agOrderTask.setTaskStatus(taskStatusEnum.getType());
            Long contentId = agOrderTaskImportReq.getContentId();
            Long fancyItemId = agOrderTaskImportReq.getFancyItemId();
            // 查询部门id
            Long merchantId = agOrderTaskImportReq.getAgMerchantId();
            List<AgMerchant> agMerchants = agMerchantService.list(Wrappers.lambdaQuery(AgMerchant.class)
                    .eq(AgMerchant::getMerchantId, merchantId)
                    .eq(AgMerchant::getDeleted, 0)
            );
            if (CollectionUtil.isEmpty(agMerchants)) {
                agUploadTaskErrorVo.setContentId(contentId);
                agUploadTaskErrorVo.setFancyItemId(fancyItemId);
                agUploadTaskErrorVo.setErrorMsg("代理商不存在");
                agUploadTaskErrorVos.add(agUploadTaskErrorVo);
                continue;
            }
            agOrderTask.setDeptId(agMerchants.get(0).getDeptId());
            // 查询创建人名称
            UserRespDTO user = userApi.getUser(agOrderTaskImportReq.getCreateId());
            if (user == null) {
                agUploadTaskErrorVo.setContentId(contentId);
                agUploadTaskErrorVo.setFancyItemId(fancyItemId);
                agUploadTaskErrorVo.setErrorMsg("创建人不存在");
                agUploadTaskErrorVos.add(agUploadTaskErrorVo);
                continue;
            }
            agOrderTask.setCreateName(user.getUsername());
            // 判断是否超过订单剩余数
            List<AgMerchantOrderDetail> merchantOrderDetailList = agMerchantOrderDetailService.list(Wrappers.lambdaQuery(AgMerchantOrderDetail.class)
                    .eq(AgMerchantOrderDetail::getAgMerchantId, merchantId)
                    .eq(AgMerchantOrderDetail::getContentServiceId, contentId)
                    .eq(AgMerchantOrderDetail::getServiceStatus, 0)
                    .eq(AgMerchantOrderDetail::getDeleted, 0)
                    .orderByAsc(AgMerchantOrderDetail::getCreateTime)
            );
            Optional<AgMerchantOrderDetail> merchantOrderDetailOpt = Optional.ofNullable(merchantOrderDetailList)
                    .orElse(new ArrayList<>())
                    .stream()
                    .filter(i -> i.getServiceTotalNum() - i.getServiceConsumeNum() > 0)
                    .findFirst();
            if (merchantOrderDetailOpt.isEmpty()) {
                agUploadTaskErrorVo.setContentId(contentId);
                agUploadTaskErrorVo.setFancyItemId(fancyItemId);
                agUploadTaskErrorVo.setErrorMsg("订单剩余数为0");
                agUploadTaskErrorVos.add(agUploadTaskErrorVo);
                continue;
            }
            AgMerchantOrderDetail agMerchantOrderDetail = merchantOrderDetailOpt.get();

            // 插入任务表,存在更新
            long taskNum = this.count(Wrappers.lambdaQuery(AgOrderTask.class)
                    .eq(AgOrderTask::getId, agOrderTask.getId())
            );
            if (taskNum > 0) {
                // 更新
                this.update(agOrderTask, null);
            } else {
                // 插入
                this.save(agOrderTask);
            }
            // 处理流水
            this.handleFlow(agOrderTask, agMerchantOrderDetail.getId());
            successCount++;
        }
        agUploadTaskReqVO.setAgUploadTaskErrorVoList(agUploadTaskErrorVos);
        agUploadTaskReqVO.setSuccessCount(successCount);
        return agUploadTaskReqVO;
    }

    /**
     * 处理流水
     * @param: agOrderTask
     * @return: void
     * @author xingchen
     * @date: 2024/6/14 18:06
     */
    private void handleFlow(AgOrderTask agOrderTask, Long orderDetailId) {
        // 判断是否状态为完成或交付,并且不存在流水表中
        if (!Objects.equals(agOrderTask.getTaskStatus(), TaskStatusEnum.COMPLETED.getType())
                || !Objects.equals(agOrderTask.getTaskStatus(), TaskStatusEnum.DELIVERED.getType())) {
            return;
        }
        long flowCount = agOrderFlowService.count(Wrappers.lambdaQuery(AgOrderFlow.class)
                .eq(AgOrderFlow::getTaskId, agOrderTask.getId())
        );
        if (flowCount > 0) {
            return;
        }
        // 增加消耗数,订单主表和子表,插入流水表
        AgMerchantOrderDetail merchantOrderDetail = agMerchantOrderDetailService.getById(orderDetailId);
        merchantOrderDetail.setServiceConsumeNum(merchantOrderDetail.getServiceConsumeNum() + 1);
        agMerchantOrderDetailService.updateById(merchantOrderDetail);

        AgMerchantOrder agMerchantOrder = agMerchantOrderService.getById(merchantOrderDetail.getAgMerchantOrderId());
        agMerchantOrder.setServiceConsumeNum(agMerchantOrder.getServiceConsumeNum() + 1);
        agMerchantOrderService.updateById(agMerchantOrder);

        AgOrderFlow agOrderFlow = new AgOrderFlow();
        BeanUtil.copyProperties(agOrderTask, agOrderFlow, "id");
        agOrderFlow.setTaskId(agOrderTask.getId());
        agOrderFlow.setDeductPoint(1);
        agOrderFlow.setOrderId(merchantOrderDetail.getAgMerchantOrderId());
        agOrderFlowService.save(agOrderFlow);
    }

}
