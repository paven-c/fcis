package com.fancy.module.agent.convert.agent;


import com.fancy.common.util.collection.CollectionUtils;
import com.fancy.module.agent.api.agent.dto.AgentSaveReqDTO;
import com.fancy.module.agent.controller.vo.AgentRespVO;
import com.fancy.module.agent.controller.vo.AgentSaveReqVO;
import com.fancy.module.agent.repository.pojo.agent.Agent;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author paven
 */
@Mapper
public interface AgentConvert {

    AgentConvert INSTANCE = Mappers.getMapper(AgentConvert.class);

    @Mapping(target = "parentAgentId", source = "parentAgentId")
    @Mapping(target = "deptId", source = "deptId")
    AgentSaveReqVO convert(AgentSaveReqDTO bean, Long deptId, Long parentAgentId);

    AgentRespVO convertVO(Agent bean);

    default List<AgentRespVO> convertList(List<Agent> list) {
        return CollectionUtils.convertList(list, this::convertVO);
    }

}
