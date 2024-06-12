package com.fancy.module.agent.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fancy.module.agent.repository.pojo.AgOrderFlow;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单扣减流水表 Mapper 接口
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@Mapper
public interface AgOrderFlowMapper extends BaseMapper<AgOrderFlow> {

}
