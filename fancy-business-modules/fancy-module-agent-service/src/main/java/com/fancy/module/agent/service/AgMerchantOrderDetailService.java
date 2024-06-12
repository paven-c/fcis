package com.fancy.module.agent.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fancy.module.agent.api.task.dto.MerchantTaskNumListDTO;
import com.fancy.module.agent.repository.pojo.AgMerchantOrderDetail;
import com.fancy.module.agent.controller.vo.MerchantTaskNumListVO;
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

    List<MerchantTaskNumListVO> listMerchantTaskNum(MerchantTaskNumListDTO merchantTaskNumListDTO);
}
