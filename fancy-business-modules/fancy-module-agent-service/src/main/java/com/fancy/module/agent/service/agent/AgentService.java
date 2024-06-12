package com.fancy.module.agent.service.agent;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fancy.common.pojo.PageResult;
import com.fancy.module.agent.controller.vo.AgentPageReqVO;
import com.fancy.module.agent.controller.vo.AgentSaveReqVO;
import com.fancy.module.agent.repository.pojo.agent.Agent;

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

    /**
     * 根据ID查询代理商
     *
     * @param parentAgentId 代理商ID
     * @return 代理
     */
    Agent selectById(Long parentAgentId);

}
