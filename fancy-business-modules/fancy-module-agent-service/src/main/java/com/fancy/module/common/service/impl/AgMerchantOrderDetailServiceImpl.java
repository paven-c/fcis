package com.fancy.module.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.common.pojo.CommonResult;
import com.fancy.module.common.repository.dto.MerchantTaskNumListDTO;
import com.fancy.module.common.repository.mapper.AgMerchantOrderDetailMapper;
import com.fancy.module.common.repository.pojo.AgMerchantOrderDetail;
import com.fancy.module.common.repository.vo.MerchantTaskNumListVO;
import com.fancy.module.common.service.AgMerchantOrderDetailService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单子表 服务实现类
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@Service
public class AgMerchantOrderDetailServiceImpl extends ServiceImpl<AgMerchantOrderDetailMapper, AgMerchantOrderDetail> implements
        AgMerchantOrderDetailService {

    @Override
    public CommonResult<List<MerchantTaskNumListVO>> listMerchantTaskNum(MerchantTaskNumListDTO merchantTaskNumListDTO) {

        return null;
    }
}
