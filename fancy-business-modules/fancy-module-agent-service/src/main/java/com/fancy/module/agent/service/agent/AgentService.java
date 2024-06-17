package com.fancy.module.agent.service.agent;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fancy.common.pojo.PageResult;
import com.fancy.component.datapermission.core.annotation.DataPermission;
import com.fancy.module.agent.controller.vo.AgentPageReqVO;
import com.fancy.module.agent.controller.vo.AgentSaveReqVO;
import com.fancy.module.agent.repository.pojo.agent.Agent;
import java.util.List;
import java.util.Set;

/**
 * @author paven
 */
public interface AgentService extends IService<Agent> {

    /**
     * 代理商分页列表
     *
     * @param pageReqVO 分页参数
     * @return 分页列表
     */
    PageResult<Agent> getAgentPage(AgentPageReqVO pageReqVO);

    /**
     * 新增代理商
     *
     * @param reqVO 请求参数
     * @return 代理商ID
     */
    Long createAgent(AgentSaveReqVO reqVO);

    /**
     * 更新代理商
     *
     * @param reqVO 请求参数
     */
    void updateAgent(AgentSaveReqVO reqVO);

    /**
     * 删除代理商
     *
     * @param agentId 代理商ID
     */
    void deleteAgent(Long agentId);

    /**
     * 根据ID查询代理商
     *
     * @param agentId 代理商ID
     * @return 代理
     */
    Agent getAgent(Long agentId);

    Agent getAgentByUserId(Long userId);

    /**
     * 获取用户对应的代理商信息 不走数据权限
     * @param userId
     * @return
     */
    @DataPermission(enable = false)
    List<Agent> getAgentByUserIdWithoutDataPermission(List<Long> userId);

    /**
     * 查询代理商列表
     *
     * @param pageReqVO 分页参数
     * @return 代理商列表
     */
    List<Agent> getAgentList(AgentPageReqVO pageReqVO);

    /**
     * 根据ID查询代理商
     *
     * @param agentId 代理商ID
     * @return 代理
     */
    Agent selectById(Long agentId);

    /**
     * 根据ID查询代理商
     *
     * @param agentId 代理商ID
     * @return 代理
     */
    long selectCountByParentId(Long agentId);

    /**
     * 根据ID查询代理商
     *
     * @param agentIds 代理商ID
     * @return 代理
     */
    List<Agent> selectByIdsWithoutDataPermission(Set<Long> agentIds);

    /**
     * 查询代理商列表
     *
     * @param pageReqVO 分页参数
     * @return 代理商列表
     */
    List<Agent> getAgentListWithoutDataPermission(AgentPageReqVO pageReqVO);

    /**
     * 根据代理商ID查询代理商
     *
     * @param agentId 代理商ID
     * @return 代理商
     */
    Agent getAgentWithoutDataPermission(Long agentId);

}
