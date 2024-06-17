package com.fancy.module.agent.service.agent;

import static com.fancy.common.exception.util.ServiceExceptionUtil.exception;
import static com.fancy.module.agent.enums.AgentStatusEnum.COMPANY_AVAILABLE_STATUS;
import static com.fancy.module.common.enums.ErrorCodeConstants.AGENT_MOBILE_DUPLICATE;
import static com.fancy.module.common.enums.ErrorCodeConstants.AGENT_NAME_DUPLICATE;
import static com.fancy.module.common.enums.ErrorCodeConstants.AGENT_NOT_EXISTS;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.common.enums.DeleteStatusEnum;
import com.fancy.common.pojo.PageResult;
import com.fancy.component.datapermission.core.annotation.DataPermission;
import com.fancy.component.datapermission.core.util.DataPermissionUtils;
import com.fancy.component.mybatis.core.query.LambdaQueryWrapperX;
import com.fancy.module.agent.controller.vo.AgentPageReqVO;
import com.fancy.module.agent.controller.vo.AgentSaveReqVO;
import com.fancy.module.agent.repository.mapper.agent.AgentMapper;
import com.fancy.module.agent.repository.pojo.agent.Agent;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author paven
 */
@Service
public class AgentServiceImpl extends ServiceImpl<AgentMapper, Agent> implements AgentService {

    @Resource
    private AgentMapper agentMapper;

    @Override
    public PageResult<Agent> getAgentPage(AgentPageReqVO reqVO) {
        return agentMapper.selectPage(reqVO, new LambdaQueryWrapperX<Agent>()
                .in(BooleanUtil.isTrue(reqVO.getIsCompanyRole()), Agent::getStatus, COMPANY_AVAILABLE_STATUS)
                .eqIfPresent(Agent::getId, reqVO.getAgentId())
                .likeIfPresent(Agent::getAgentName, reqVO.getAgentName())
                .eqIfPresent(Agent::getMobile, reqVO.getMobile())
                .eqIfPresent(Agent::getStatus, reqVO.getStatus())
                .eqIfPresent(Agent::getLevel, reqVO.getLevel())
                .geIfPresent(Agent::getCreateTime, reqVO.getStartTime())
                .leIfPresent(Agent::getCreateTime, reqVO.getEndTime())
                .orderByDesc(Agent::getId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createAgent(AgentSaveReqVO reqVO) {
        validateAgent4CreateOrUpdate(null, reqVO.getAgentName(), reqVO.getMobile());
        // 代理商信息
        Agent agent = Agent.builder().agentName(reqVO.getAgentName()).provinceId(reqVO.getProvinceId()).cityId(reqVO.getCityId()).status(reqVO.getStatus())
                // 联系人信息
                .level(reqVO.getLevel()).parentId(reqVO.getParentAgentId()).mobile(reqVO.getMobile()).contactor(reqVO.getContactorName())
                // 身份证信息
                .idCardFrontSide(reqVO.getIdCardFrontSide()).idCardBackSide(reqVO.getIdCardBackSide())
                // 营业执照、合同信息
                .businessLicense(reqVO.getBusinessLicense()).contractLink(reqVO.getContractLink()).introduction(reqVO.getIntroduction())
                // 合作起始时间 & 合作状态
                .beginTime(reqVO.getBeginTime()).endTime(reqVO.getEndTime()).userId(reqVO.getUserId()).deptId(reqVO.getDeptId()).build();
        agentMapper.insert(agent);
        return agent.getId();
    }

    @Override
    public void updateAgent(AgentSaveReqVO reqVO) {
        validateAgent4CreateOrUpdate(reqVO.getId(), reqVO.getAgentName(), reqVO.getMobile());
        // 代理商信息
        Agent updateAgent = Agent.builder().id(reqVO.getId()).agentName(reqVO.getAgentName()).provinceId(reqVO.getProvinceId()).cityId(reqVO.getCityId())
                // 联系人信息
                .parentId(reqVO.getParentAgentId()).mobile(reqVO.getMobile()).contactor(reqVO.getContactorName())
                // 身份证信息
                .idCardFrontSide(reqVO.getIdCardFrontSide()).idCardBackSide(reqVO.getIdCardBackSide())
                // 营业执照、合同信息
                .businessLicense(reqVO.getBusinessLicense()).contractLink(reqVO.getContractLink()).introduction(reqVO.getIntroduction())
                // 合作起始时间 & 合作状态
                .beginTime(reqVO.getBeginTime()).endTime(reqVO.getEndTime()).status(reqVO.getStatus()).approveTime(reqVO.getApproveTime()).build();
        agentMapper.updateById(updateAgent);
    }

    @Override
    public void deleteAgent(Long agentId) {
        if (agentMapper.selectCountByParentId(agentId) > 0) {
            throw exception(AGENT_NOT_EXISTS);
        }
        agentMapper.update(Wrappers.lambdaUpdate(Agent.class)
                .set(Agent::getDeleted, DeleteStatusEnum.DELETED.getStatus())
                .eq(Agent::getId, agentId));
    }

    @Override
    public Agent getAgent(Long agentId) {
        return agentMapper.selectOne(Agent::getId, agentId, Agent::getDeleted, DeleteStatusEnum.ACTIVATED.getStatus());
    }

    @Override
    public Agent getAgentByUserId(Long userId) {
        return agentMapper.selectOne(Agent::getUserId, userId, Agent::getDeleted, DeleteStatusEnum.ACTIVATED.getStatus());
    }

    @Override
    public List<Agent> getAgentByUserId(List<Long> userId) {
        return agentMapper.selectList(Wrappers.lambdaQuery(Agent.class).in(Agent::getUserId, userId).eq(Agent::getDeleted, DeleteStatusEnum.ACTIVATED.getStatus()));
    }

    @Override
    public List<Agent> getAgentList(AgentPageReqVO reqVO) {
        return agentMapper.selectList(new LambdaQueryWrapperX<Agent>()
                .eqIfPresent(Agent::getId, reqVO.getAgentId())
                .likeIfPresent(Agent::getAgentName, reqVO.getAgentName())
                .eqIfPresent(Agent::getMobile, reqVO.getMobile())
                .eqIfPresent(Agent::getStatus, reqVO.getStatus())
                .eqIfPresent(Agent::getLevel, reqVO.getLevel())
                .geIfPresent(Agent::getCreateTime, reqVO.getStartTime())
                .leIfPresent(Agent::getCreateTime, reqVO.getEndTime())
                .orderByDesc(Agent::getId));
    }

    @Override
    public Agent selectById(Long agentId) {
        return agentMapper.selectOne(Agent::getId, agentId, Agent::getDeleted, DeleteStatusEnum.ACTIVATED.getStatus());
    }

    @Override
    public long selectCountByParentId(Long agentId) {
        return agentMapper.selectCount(
                Wrappers.lambdaQuery(Agent.class).eq(Agent::getParentId, agentId).eq(Agent::getDeleted, DeleteStatusEnum.ACTIVATED.getStatus()));
    }

    @Override
    @DataPermission(enable = false)
    public List<Agent> selectByIdsWithoutDataPermission(Set<Long> parenAgentIds) {
        return agentMapper.selectList(Wrappers.lambdaQuery(Agent.class).in(Agent::getId, parenAgentIds));
    }

    @Override
    @DataPermission(enable = false)
    public List<Agent> getAgentListWithoutDataPermission(AgentPageReqVO reqVO) {
        return agentMapper.selectList(new LambdaQueryWrapperX<Agent>()
                .eqIfPresent(Agent::getId, reqVO.getAgentId())
                .likeIfPresent(Agent::getAgentName, reqVO.getAgentName())
                .eqIfPresent(Agent::getMobile, reqVO.getMobile())
                .eqIfPresent(Agent::getStatus, reqVO.getStatus())
                .eqIfPresent(Agent::getLevel, reqVO.getLevel())
                .inIfPresent(Agent::getDeptId, reqVO.getDeptIds())
                .orderByDesc(Agent::getId));
    }

    @Override
    @DataPermission(enable = false)
    public Agent getAgentWithoutDataPermission(Long agentId) {
        if (agentId == null) {
            return null;
        }
        Agent agent = selectById(agentId);
        if (agent == null) {
            throw exception(AGENT_NOT_EXISTS);
        }
        return agent;
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
