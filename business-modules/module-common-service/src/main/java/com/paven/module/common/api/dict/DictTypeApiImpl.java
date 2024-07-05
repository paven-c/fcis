package com.paven.module.common.api.dict;

import com.paven.common.enums.CommonStatusEnum;
import com.paven.module.common.controller.dict.vo.type.DictTypeSaveReqVO;
import com.paven.module.common.service.dict.DictTypeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 字典数据 API 实现类
 *
 * @author paven
 */
@Service
public class DictTypeApiImpl implements DictTypeApi {

    @Resource
    private DictTypeService dictTypeService;

    @Override
    public Long createDictType(String dictName, String dictType) {
        return dictTypeService.createDictType(DictTypeSaveReqVO.builder().name(dictName).type(dictType).status(CommonStatusEnum.ENABLE.getStatus()).build());
    }
}
