package com.fancy.module.agent.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.common.pojo.PageResult;
import com.fancy.module.agent.api.task.dto.OrderTaskListDTO;
import com.fancy.module.agent.controller.req.AgOrderTaskImportReq;
import com.fancy.module.agent.controller.vo.AgUploadTaskReqVO;
import com.fancy.module.agent.repository.mapper.AgOrderTaskMapper;
import com.fancy.module.agent.repository.pojo.AgContentServiceMain;
import com.fancy.module.agent.repository.pojo.AgMerchant;
import com.fancy.module.agent.repository.pojo.AgOrderTask;
import com.fancy.module.agent.controller.vo.OrderTaskListVO;
import com.fancy.module.agent.service.AgContentServiceMainService;
import com.fancy.module.agent.service.AgMerchantService;
import com.fancy.module.agent.service.AgOrderTaskService;
import com.fancy.module.common.api.user.UserApi;
import com.fancy.module.common.api.user.dto.UserRespDTO;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
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
                .eq(Objects.nonNull(orderTaskListDTO.getFancyItemId()), AgOrderTask::getFancyItemId, orderTaskListDTO.getFancyItemId())
                .like(StringUtils.isNotBlank(orderTaskListDTO.getFancyItemName()), AgOrderTask::getFancyItemName, orderTaskListDTO.getFancyItemName())
                .ge(Objects.nonNull(orderTaskListDTO.getTaskCreateTime()), AgOrderTask::getTaskCreateTime, orderTaskListDTO.getTaskCreateTime())
                .le(Objects.nonNull(orderTaskListDTO.getTaskFinishTime()), AgOrderTask::getTaskFinishTime, orderTaskListDTO.getTaskFinishTime())
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
    public List<AgUploadTaskReqVO> csvUploadTask(List<AgOrderTaskImportReq> agOrderTaskImportReqs) {
        List<AgUploadTaskReqVO> agUploadTaskReqVOS = new ArrayList<>();
        for (AgOrderTaskImportReq agOrderTaskImportReq : agOrderTaskImportReqs) {
            AgUploadTaskReqVO agUploadTaskReqVO = new AgUploadTaskReqVO();
            AgOrderTask agOrderTask = new AgOrderTask();
            BeanUtil.copyProperties(agOrderTaskImportReq,agOrderTask);
            Long contentId = agOrderTaskImportReq.getContentId();
            Long fancyItemId = agOrderTaskImportReq.getFancyItemId();
            // 查询部门id
            Long merchantId = agOrderTaskImportReq.getAgMerchantId();
            agMerchantService.list(Wrappers.lambdaQuery(AgMerchant.class)
                    .eq(AgMerchant::getMerchantId, merchantId)
            );
            // 查询创建人名称
            UserRespDTO user = userApi.getUser(agOrderTaskImportReq.getCreateId());
            if (user == null) {
                agUploadTaskReqVO.setContentId(contentId);
                agUploadTaskReqVO.setFancyItemId(fancyItemId);
                agUploadTaskReqVO.setErrorMsg("创建人不存在");
                agUploadTaskReqVOS.add(agUploadTaskReqVO);
                continue;
            }
            agOrderTask.setCreateName(user.getUsername());
            // 插入任务表,存在更新
            long taskNum = this.count(Wrappers.lambdaQuery(AgOrderTask.class)
                    .eq(AgOrderTask::getId, agOrderTask.getId())
            );
            if (taskNum > 0) {
                // 更新

            } else {
                // 插入

            }

            // 判断是否状态为完成,并且不存在流水表中

            // 增加消耗数,订单主表和子表,插入流水表

        }
        return null;
    }

}
