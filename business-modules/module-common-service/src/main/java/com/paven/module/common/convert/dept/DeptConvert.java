package com.paven.module.common.convert.dept;


import com.paven.module.common.api.dept.dto.DeptRespDTO;
import com.paven.module.common.api.dept.dto.DeptSaveReqDTO;
import com.paven.module.common.controller.dept.vo.DeptSaveReqVO;
import com.paven.module.common.repository.pojo.dept.Dept;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author paven
 */
@Mapper
public interface DeptConvert {

    DeptConvert INSTANCE = Mappers.getMapper(DeptConvert.class);

    DeptSaveReqVO convert(DeptSaveReqDTO bean);

    DeptRespDTO convertDTO(Dept dept);
}
