package com.fancy.module.agent.service.agent;

import static com.fancy.common.exception.util.ServiceExceptionUtil.exception;
import static com.fancy.module.common.enums.ErrorCodeConstants.AGENT_MOBILE_DUPLICATE;
import static com.fancy.module.common.enums.ErrorCodeConstants.AGENT_NAME_DUPLICATE;
import static com.fancy.module.common.enums.ErrorCodeConstants.AGENT_NOT_EXISTS;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.common.enums.DeleteStatusEnum;
import com.fancy.common.pojo.PageResult;
import com.fancy.component.datapermission.core.util.DataPermissionUtils;
import com.fancy.component.mybatis.core.query.LambdaQueryWrapperX;
import com.fancy.module.agent.controller.vo.AgentPageReqVO;
import com.fancy.module.agent.controller.vo.AgentSaveReqVO;
import com.fancy.module.agent.repository.mapper.agent.AgentMapper;
import com.fancy.module.agent.repository.pojo.agent.Agent;
import com.fancy.module.common.api.dept.DeptApi;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author paven
 */
@Service
public class AgentServiceImpl extends ServiceImpl<AgentMapper, Agent> implements AgentService {

    @Resource
    private AgentMapper agentMapper;
    @Resource
    private DeptApi deptApi;

    @Override
    public PageResult<Agent> getAgentPage(AgentPageReqVO reqVO) {
        return agentMapper.selectPage(reqVO, new LambdaQueryWrapperX<Agent>()
                .likeIfPresent(Agent::getAgentName, reqVO.getAgentName())
                .eqIfPresent(Agent::getMobile, reqVO.getMobile())
                .eqIfPresent(Agent::getStatus, reqVO.getStatus())
                .betweenIfPresent(Agent::getCreateTime, reqVO.getCreateTime())
                .inIfPresent(Agent::getDeptId, deptApi.getChildDeptList(reqVO.getDeptId()))
                .orderByDesc(Agent::getId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createAgent(AgentSaveReqVO reqVO) {
        validateAgent4CreateOrUpdate(null, reqVO.getAgentName(), reqVO.getMobile());
        // 代理商信息
        Agent agent = Agent.builder().agentName(reqVO.getAgentName()).provinceId(reqVO.getProvinceId()).cityId(reqVO.getCityId())
                // 联系人信息
                .level(reqVO.getLevel()).parentId(reqVO.getParentAgentId()).mobile(reqVO.getMobile()).contactor(reqVO.getContactorName())
                // 身份证信息
                .idCardFrontSide(reqVO.getIdCardFrontSide()).idCardBackSide(reqVO.getIdCardBackSide())
                // 营业执照、合同信息
                .businessLicense(reqVO.getBusinessLicense()).contractLink(reqVO.getContractLink()).introduction(reqVO.getIntroduction())
                // 合作起始时间 & 合作状态
                .beginTime(reqVO.getBeginTime()).endTime(reqVO.getEndTime()).deptId(reqVO.getDeptId()).status(reqVO.getStatus()).build();
        agentMapper.insert(agent);
        return agent.getId();
    }

    @Override
    public void updateAgent(AgentSaveReqVO reqVO) {
        validateAgent4CreateOrUpdate(reqVO.getId(), reqVO.getAgentName(), reqVO.getMobile());
        // 代理商信息
        Agent agent = Agent.builder().agentName(reqVO.getAgentName()).provinceId(reqVO.getProvinceId()).cityId(reqVO.getCityId())
                // 联系人信息
                .parentId(reqVO.getParentAgentId()).mobile(reqVO.getMobile()).contactor(reqVO.getContactorName())
                // 身份证信息
                .idCardFrontSide(reqVO.getIdCardFrontSide()).idCardBackSide(reqVO.getIdCardBackSide())
                // 营业执照、合同信息
                .businessLicense(reqVO.getBusinessLicense()).contractLink(reqVO.getContractLink()).introduction(reqVO.getIntroduction())
                // 合作起始时间 & 合作状态
                .beginTime(reqVO.getBeginTime()).endTime(reqVO.getEndTime()).build();
        agentMapper.insert(agent);
    }

    @Override
    public void deleteAgent(Long agentId) {
        if (agentMapper.selectCountByParentId(agentId) > 0) {
            throw exception(AGENT_NOT_EXISTS);
        }
        agentMapper.deleteById(agentId);
    }

    @Override
    public Agent getAgent(Long agentId) {
        return agentMapper.selectOne(Agent::getId, agentId, Agent::getDeleted, DeleteStatusEnum.ACTIVATED.getStatus());
    }

    @Override
    public Agent selectById(Long agentId) {
        return agentMapper.selectOne(Agent::getId, agentId, Agent::getDeleted, DeleteStatusEnum.ACTIVATED.getStatus());
    }

    private Agent validateAgent4CreateOrUpdate(Long agentId, String agentName, String mobile) {
        return DataPermissionUtils.executeIgnore(() -> {
            // 校验代理商是否存在
            Agent agent = validateAgentExists(agentId);
            // 校验代理商名称
            validateAgentNameUnique(agentId, agentName);
            // 校验代理商手机号码
            validateAgentMobileUnique(agentId, mobile);
            return agent;
        });
    }

    private void validateAgentMobileUnique(Long agentId, String mobile) {
        if (StrUtil.isBlank(mobile)) {
            return;
        }
        Agent agent = agentMapper.selectByMobile(mobile);
        if (agent == null) {
            return;
        }
        if (!agent.getId().equals(agentId)) {
            throw exception(AGENT_MOBILE_DUPLICATE);
        }
    }

    private void validateAgentNameUnique(Long agentId, String agentName) {
        if (StrUtil.isBlank(agentName)) {
            return;
        }
        Agent agent = agentMapper.selectByAgentName(agentName);
        if (agent == null) {
            return;
        }
        if (!agent.getId().equals(agentId)) {
            throw exception(AGENT_NAME_DUPLICATE);
        }
    }

    private Agent validateAgentExists(Long agentId) {
        if (agentId == null) {
            return null;
        }
        Agent agent = agentMapper.selectOne(Agent::getId, agentId, Agent::getDeleted, DeleteStatusEnum.ACTIVATED.getStatus());
        if (agent == null) {
            throw exception(AGENT_NOT_EXISTS);
        }
        return agent;
    }
}