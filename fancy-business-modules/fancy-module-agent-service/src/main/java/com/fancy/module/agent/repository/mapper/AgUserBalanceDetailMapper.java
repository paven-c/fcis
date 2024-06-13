package com.fancy.module.agent.repository.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fancy.component.mybatis.core.mapper.BaseMapperX;
import com.fancy.module.agent.repository.pojo.AgUserBalanceDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 订单任务表 Mapper 接口
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@Mapper
public interface AgUserBalanceDetailMapper extends BaseMapperX<AgUserBalanceDetail> {

    int updateBalance(@Param("id") Long id,@Param("balance") BigDecimal balance,@Param("billType") Integer billType);
}
