package com.fancy.module.agent.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fancy.component.mybatis.core.mapper.BaseMapperX;
import com.fancy.module.agent.controller.vo.AgMerchantOrderOverviewVo;
import com.fancy.module.agent.repository.pojo.AgMerchantOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 订单任务表 Mapper 接口
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@Mapper
public interface AgMerchantOrderMapper extends BaseMapperX<AgMerchantOrder> {

    public AgMerchantOrderOverviewVo countAgMerchantOrderOverviewVo(@Param("creator_id") List<Long> creatorIds);

}
