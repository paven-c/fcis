package com.fancy.module.common.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fancy.module.common.repository.pojo.AgMerchantOrder;
import com.fancy.module.common.repository.pojo.AgMerchantOrderDetail;
import com.fancy.module.common.repository.vo.MerchantTaskNumListVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 订单任务表 Mapper 接口
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@Mapper
public interface AgMerchantOrderDetailMapper extends BaseMapper<AgMerchantOrderDetail> {

    List<MerchantTaskNumListVO> listMerchantTaskNum(@Param("merchantId") Long merchantId);

}