package com.fancy.module.common.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fancy.common.pojo.CommonResult;
import com.fancy.module.common.repository.dto.MerchantTaskNumListDTO;
import com.fancy.module.common.repository.pojo.AgMerchantOrder;
import com.fancy.module.common.repository.pojo.AgMerchantOrderDetail;
import com.fancy.module.common.repository.vo.MerchantTaskNumListVO;
import java.util.List;

/**
 * <p>
 * 订单扣减流水表 服务类
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
public interface AgMerchantOrderDetailService extends IService<AgMerchantOrderDetail> {

    CommonResult<List<MerchantTaskNumListVO>> listMerchantTaskNum(MerchantTaskNumListDTO merchantTaskNumListDTO);
}
