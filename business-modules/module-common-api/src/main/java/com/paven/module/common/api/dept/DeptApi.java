package com.paven.module.common.api.dept;

import com.paven.module.common.api.dept.dto.DeptRespDTO;
import com.paven.module.common.api.dept.dto.DeptSaveReqDTO;
import jakarta.validation.Valid;
import java.util.Set;

/**
 * 部门API
 *
 * @author paven
 */
public interface DeptApi {

    /**
     * 新增部门
     *
     * @param reqDTO 新增部门参数
     * @return 部门编号
     */
    Long createDept(@Valid DeptSaveReqDTO reqDTO);

    /**
     * 更新部门
     *
     * @param reqDTO 更新部门参数
     */
    void updateDept(DeptSaveReqDTO reqDTO);

    /**
     * 获取部门
     *
     * @param deptId 部门ID
     * @return 部门
     */
    DeptRespDTO getDept(Long deptId);

    /**
     * 获取部门列表
     *
     * @param deptId 部门编号
     * @return 部门列表
     */
    Set<Long> getChildDeptList(Long deptId);

}
