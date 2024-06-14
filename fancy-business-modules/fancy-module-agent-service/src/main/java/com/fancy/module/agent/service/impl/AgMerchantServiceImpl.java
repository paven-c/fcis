package com.fancy.module.agent.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.common.pojo.PageResult;
import com.fancy.component.security.core.util.SecurityFrameworkUtils;
import com.fancy.module.agent.controller.req.EditAgMerchantReq;
import com.fancy.module.agent.controller.req.QueryAgMerchantReq;
import com.fancy.module.agent.controller.vo.AgMerchantVo;
import com.fancy.module.agent.convert.merchant.AgMerchantConvert;
import com.fancy.module.agent.repository.mapper.AgMerchantMapper;
import com.fancy.module.agent.repository.pojo.AgMerchant;
import com.fancy.module.agent.service.AgMerchantService;
import com.fancy.module.common.api.content.CmsMerchantApi;
import com.fancy.module.common.api.content.Dto.CmsMerchantReqDto;
import com.fancy.module.common.api.user.UserApi;
import com.fancy.module.common.api.user.dto.UserRespDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.fancy.component.security.core.util.SecurityFrameworkUtils.getLoginUserDeptId;
import static com.fancy.component.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * <p>
 * 订单扣减流水表 服务实现类
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@Service
public class AgMerchantServiceImpl extends ServiceImpl<AgMerchantMapper, AgMerchant> implements AgMerchantService {

    @Resource
    private CmsMerchantApi cmsMerchantApi;
    @Resource
    private UserApi userApi;

    @Override
    @Transactional
    public void addMerchant(EditAgMerchantReq req) {
        //先保存代理商户 再更新
        Long loginUserId = getLoginUserId();
        Long loginUserDeptId = getLoginUserDeptId();
        req.setCreatorId(loginUserId);
        req.setDeptId(loginUserDeptId);
        LocalDateTime now = LocalDateTime.now();
        req.setCreateTime(now);
        req.setUpdateTime(now);
        AgMerchant agMerchant = AgMerchantConvert.INSTANCE.convertAgMerchant(req);
        //登录人Id和部门Id
        save(agMerchant);
        CmsMerchantReqDto cmsMerchantReqDto = AgMerchantConvert.INSTANCE.convertCmsMerchantReqDto(req);
        // cmsMerchantConfig/callback/agentCreateMerchant 创建商户
        Long checkedData = cmsMerchantApi.agentCreateMerchant(cmsMerchantReqDto).getCheckedData();
        agMerchant.setMerchantId(checkedData);
        updateById(agMerchant);
    }

    @Override
    public void updateMerchant(EditAgMerchantReq req) {
        AgMerchant agMerchant = AgMerchantConvert.INSTANCE.convertAgMerchant(req);
        req.setUpdateTime(LocalDateTime.now());
        updateById(agMerchant);
        CmsMerchantReqDto cmsMerchantReqDto = AgMerchantConvert.INSTANCE.convertCmsMerchantReqDto(req);
        cmsMerchantApi.agentCreateMerchant(cmsMerchantReqDto).getCheckedData();
    }

    @Override
    public PageResult<AgMerchantVo> pageListMerchant(QueryAgMerchantReq req) {
        PageResult<AgMerchant> agMerchantPageResult = baseMapper.selectPage(req, Wrappers.lambdaQuery(AgMerchant.class)
                .eq(ObjectUtil.isNotEmpty(req.getAgMerchantId()), AgMerchant::getId, req.getAgMerchantId())
                .eq(ObjectUtil.isNotEmpty(req.getAuthStatus()), AgMerchant::getAuthStatus, req.getAuthStatus())
                .like(ObjectUtil.isNotEmpty(req.getName()), AgMerchant::getName, req.getName())
                .in(ObjectUtil.isNotEmpty(req.getCreatorIds()), AgMerchant::getCreatorId, req.getCreatorIds())
                .between(
                        ObjectUtil.isNotEmpty(req.getStartTime()) && ObjectUtil.isNotEmpty(req.getEndTime()), AgMerchant::getCreateTime, req.getStartTime(),
                        req.getEndTime())
                .eq(AgMerchant::getDeleted, 0)
                .orderByDesc(AgMerchant::getCreateTime));
        if (CollUtil.isEmpty(agMerchantPageResult.getList())) {
            return new PageResult<>(agMerchantPageResult.getTotal(), req.getPageNum(), req.getPageSize());
        }
        List<AgMerchantVo> agMerchantVos = AgMerchantConvert.INSTANCE.convertList(agMerchantPageResult.getList());
        List<Long> collect = agMerchantVos.stream().map(AgMerchant::getCreatorId).collect(Collectors.toList());
        Map<Long, String> userMap = userApi.getUserByIds(collect)
                .stream().collect(Collectors.toMap(UserRespDTO::getId, UserRespDTO::getUsername));
        agMerchantVos.forEach(agMerchantVo -> {
            if (ObjectUtil.isNotEmpty(agMerchantVo.getCreatorId())) {
                agMerchantVo.setAgUserName(userMap.get(agMerchantVo.getCreatorId()));
            }
        });
        return new PageResult<>(agMerchantVos, agMerchantPageResult.getTotal(), req.getPageNum(), req.getPageSize());
    }

    @Override
    public List<AgMerchantVo> listMerchant(QueryAgMerchantReq req) {
        List<AgMerchant> list = lambdaQuery()
                .in(ObjectUtil.isNotEmpty(req.getCreatorIds()), AgMerchant::getCreatorId, req.getCreatorIds())
                .eq(AgMerchant::getDeleted, 0)
                .orderByDesc(AgMerchant::getCreateTime).list();
        return AgMerchantConvert.INSTANCE.convertList(list);
    }

    @Override
    public void updateAuthStatus(EditAgMerchantReq req) {
        lambdaUpdate()
                .set(AgMerchant::getAuthStatus, req.getAuthStatus())
                .eq(AgMerchant::getMerchantId, req.getMerchantId())
                .update();
    }
}
