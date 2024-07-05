package com.paven.module.common.convert.dept;

import com.paven.module.common.api.dept.dto.DeptRespDTO;
import com.paven.module.common.api.dept.dto.DeptSaveReqDTO;
import com.paven.module.common.controller.dept.vo.DeptSaveReqVO;
import com.paven.module.common.repository.pojo.dept.Dept;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-05T16:40:03+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Amazon.com Inc.)"
)
public class DeptConvertImpl implements DeptConvert {

    @Override
    public DeptSaveReqVO convert(DeptSaveReqDTO bean) {
        if ( bean == null ) {
            return null;
        }

        DeptSaveReqVO deptSaveReqVO = new DeptSaveReqVO();

        deptSaveReqVO.setId( bean.getId() );
        deptSaveReqVO.setName( bean.getName() );
        deptSaveReqVO.setParentId( bean.getParentId() );
        deptSaveReqVO.setSort( bean.getSort() );
        deptSaveReqVO.setPhone( bean.getPhone() );
        deptSaveReqVO.setEmail( bean.getEmail() );
        deptSaveReqVO.setStatus( bean.getStatus() );

        return deptSaveReqVO;
    }

    @Override
    public DeptRespDTO convertDTO(Dept dept) {
        if ( dept == null ) {
            return null;
        }

        DeptRespDTO deptRespDTO = new DeptRespDTO();

        deptRespDTO.setId( dept.getId() );
        deptRespDTO.setParentId( dept.getParentId() );
        deptRespDTO.setSort( dept.getSort() );
        deptRespDTO.setStatus( dept.getStatus() );
        deptRespDTO.setCreateTime( dept.getCreateTime() );

        return deptRespDTO;
    }
}
