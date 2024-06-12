package com.fancy.module.agent.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.module.agent.api.task.dto.MerchantTaskNumListDTO;
import com.fancy.module.agent.repository.mapper.AgMerchantOrderDetailMapper;
import com.fancy.module.agent.repository.pojo.AgContentServiceMain;
import com.fancy.module.agent.repository.pojo.AgMerchantOrderDetail;
import com.fancy.module.agent.controller.vo.MerchantTaskNumListVO;
import com.fancy.module.agent.service.AgContentServiceMainService;
import com.fancy.module.agent.service.AgMerchantOrderDetailService;
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
                    .in(AgContentServiceMain::getId, merchantTaskNumListVOS.stream().map(MerchantTaskNumListVO::getContentServiceId).toList())
            );

            if (CollectionUtil.isNotEmpty(serviceMainList)) {
                for (MerchantTaskNumListVO merchantTaskNumListVO : merchantTaskNumListVOS) {
                    for (AgContentServiceMain serviceMain : serviceMainList) {
                        if (Objects.equals(merchantTaskNumListVO.getContentServiceId(), serviceMain.getId())) {
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
