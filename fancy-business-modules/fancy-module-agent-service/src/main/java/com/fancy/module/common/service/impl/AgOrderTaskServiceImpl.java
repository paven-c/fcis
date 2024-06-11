package com.fancy.module.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.common.pojo.PageResult;
import com.fancy.module.common.repository.dto.OrderTaskListDTO;
import com.fancy.module.common.repository.mapper.AgOrderTaskMapper;
import com.fancy.module.common.repository.pojo.AgContentServiceMain;
import com.fancy.module.common.repository.pojo.AgOrderTask;
import com.fancy.module.common.repository.vo.OrderTaskListVO;
import com.fancy.module.common.service.AgContentServiceMainService;
import com.fancy.module.common.service.AgOrderTaskService;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

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
                .eq(Objects.nonNull(orderTaskListDTO.getMerchantId()), AgOrderTask::getMerchantId, orderTaskListDTO.getMerchantId())
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
        PageResult<OrderTaskListVO> orderTaskListVOPageResult = new PageResult<>();
        BeanUtil.copyProperties(agOrderTaskPageResult, orderTaskListVOPageResult);
        orderTaskListVOPageResult.setList(orderTaskListVOS);
        return orderTaskListVOPageResult;
    }

}
