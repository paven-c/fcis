package com.fancy.module.agent.api.agent;

import com.fancy.module.agent.api.agent.dto.AgentSaveReqDTO;
import jakarta.validation.Valid;

/**
 * 代理商API
 *
 * @author paven
 */
public interface AgentApi {

    /**
     * 创建代理商
     *
     * @param createReqDTO 代理商参数
     * @return 代理商ID
     */
    Long createAgent(@Valid AgentSaveReqDTO createReqDTO);

}
