package com.fancy.module.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.common.pojo.CommonResult;
import com.fancy.module.common.repository.dto.MerchantTaskNumListDTO;
import com.fancy.module.common.repository.mapper.AgContentServiceDetailMapper;
import com.fancy.module.common.repository.pojo.AgContentServiceDetail;
import com.fancy.module.common.repository.vo.MerchantTaskNumListVO;
import com.fancy.module.common.service.AgContentServiceDetailService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 内容服务子表 服务实现类
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@Service
public class AgContentServiceDetailServiceImpl extends ServiceImpl<AgContentServiceDetailMapper, AgContentServiceDetail> implements
        AgContentServiceDetailService {

}
