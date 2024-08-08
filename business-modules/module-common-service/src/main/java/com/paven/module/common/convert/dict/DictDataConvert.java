package com.paven.module.common.convert.dict;


import com.paven.module.common.api.dept.dto.DeptRespDTO;
import com.paven.module.common.api.dept.dto.DeptSaveReqDTO;
import com.paven.module.common.api.dict.dto.DictDataDTO;
import com.paven.module.common.controller.dept.vo.DeptSaveReqVO;
import com.paven.module.common.repository.pojo.dept.Dept;
import com.paven.module.common.repository.pojo.dict.DictData;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author paven
 */
@Mapper
public interface DictDataConvert {

    DictDataConvert INSTANCE = Mappers.getMapper(DictDataConvert.class);

    DictData convert(DictDataDTO bean);

    List<DictData> convertList(List<DictDataDTO> beanList);
}
