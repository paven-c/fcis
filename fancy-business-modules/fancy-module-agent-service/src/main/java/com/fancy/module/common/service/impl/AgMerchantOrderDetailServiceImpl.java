package com.fancy.module.common.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.common.pojo.CommonResult;
import com.fancy.module.common.repository.dto.MerchantTaskNumListDTO;
import com.fancy.module.common.repository.mapper.AgMerchantOrderDetailMapper;
import com.fancy.module.common.repository.pojo.AgContentServiceMain;
import com.fancy.module.common.repository.pojo.AgMerchantOrderDetail;
import com.fancy.module.common.repository.vo.MerchantTaskNumListVO;
import com.fancy.module.common.service.AgContentServiceMainService;
import com.fancy.module.common.service.AgMerchantOrderDetailService;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Objects;
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

    @Resource
    private AgContentServiceMainService agContentServiceMainService;

    @Override
    public List<MerchantTaskNumListVO> listMerchantTaskNum(MerchantTaskNumListDTO merchantTaskNumListDTO) {
        List<MerchantTaskNumListVO> merchantTaskNumListVOS = this.baseMapper.listMerchantTaskNum(merchantTaskNumListDTO.getMerchantId());
        if (CollectionUtil.isNotEmpty(merchantTaskNumListVOS)) {
            // 查询内容名称
            List<AgContentServiceMain> serviceMainList = agContentServiceMainService.list(Wrappers.lambdaQuery(AgContentServiceMain.class)
                    .in(AgContentServiceMain::getId, merchantTaskNumListVOS.stream().map(MerchantTaskNumListVO::getContentId).toList())
            );

            if (CollectionUtil.isNotEmpty(serviceMainList)) {
                for (MerchantTaskNumListVO merchantTaskNumListVO : merchantTaskNumListVOS) {
                    for (AgContentServiceMain serviceMain : serviceMainList) {
                        if (Objects.equals(merchantTaskNumListVO.getContentId(), serviceMain.getId())) {
                            merchantTaskNumListVO.setContentServiceName(serviceMain.getContentName());
                        }
                    }
                }
            }
            return merchantTaskNumListVOS;
        }
        return null;
    }
}
