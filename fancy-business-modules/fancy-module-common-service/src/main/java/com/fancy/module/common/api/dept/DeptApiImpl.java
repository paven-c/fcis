package com.fancy.module.common.api.dept;

import static com.fancy.common.util.collection.CollectionUtils.convertSet;

import com.fancy.module.common.api.dept.dto.DeptRespDTO;
import com.fancy.module.common.api.dept.dto.DeptSaveReqDTO;
import com.fancy.module.common.convert.dept.DeptConvert;
import com.fancy.module.common.repository.pojo.dept.Dept;
import com.fancy.module.common.service.dept.DeptService;
import jakarta.annotation.Resource;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 部门API实现类
 *
 * @author paven
 */
@Service
@Validated
public class DeptApiImpl implements DeptApi {

    @Resource
    private DeptService deptService;

    @Override
    public Long createDept(DeptSaveReqDTO createReqDTO) {
        Long deptId = deptService.createDept(DeptConvert.INSTANCE.convert(createReqDTO));
        return deptId;
    }

    @Override
    public DeptRespDTO getDept(Long deptId) {
        return DeptConvert.INSTANCE.convertDTO(deptService.getDept(deptId));
    }

    @Override
    public Set<Long> getChildDeptList(Long deptId) {
        return convertSet(deptService.getChildDeptList(deptId), Dept::getId);
    }

}
