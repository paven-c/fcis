package com.fancy.module.common.controller.dept;


import static com.fancy.common.pojo.CommonResult.success;

import com.fancy.common.enums.CommonStatusEnum;
import com.fancy.common.pojo.CommonResult;
import com.fancy.common.util.object.BeanUtils;
import com.fancy.module.common.controller.dept.vo.DeptListReqVO;
import com.fancy.module.common.controller.dept.vo.DeptRespVO;
import com.fancy.module.common.controller.dept.vo.DeptSaveReqVO;
import com.fancy.module.common.controller.dept.vo.DeptSimpleRespVO;
import com.fancy.module.common.repository.pojo.dept.Dept;
import com.fancy.module.common.service.dept.DeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "部门")
@RestController
@RequestMapping("/system/dept")
@Validated
public class DeptController {

    @Resource
    private DeptService deptService;

    @PostMapping("create")
    @Operation(summary = "创建部门")
    @PreAuthorize("@ss.hasPermission('common:dept:create')")
    public CommonResult<Long> createDept(@Valid @RequestBody DeptSaveReqVO createReqVO) {
        Long deptId = deptService.createDept(createReqVO);
        return success(deptId);
    }

    @PutMapping("update")
    @Operation(summary = "更新部门")
    @PreAuthorize("@ss.hasPermission('common:dept:update')")
    public CommonResult<Boolean> updateDept(@Valid @RequestBody DeptSaveReqVO updateReqVO) {
        deptService.updateDept(updateReqVO);
        return success(true);
    }

    @DeleteMapping("delete")
    @Operation(summary = "删除部门")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('common:dept:delete')")
    public CommonResult<Boolean> deleteDept(@RequestParam("id") Long id) {
        deptService.deleteDept(id);
        return success(true);
    }

    @GetMapping("/list")
    @Operation(summary = "获取部门列表")
    @PreAuthorize("@ss.hasPermission('common:dept:query')")
    public CommonResult<List<DeptRespVO>> getDeptList(DeptListReqVO reqVO) {
        List<Dept> list = deptService.getDeptList(reqVO);
        return success(BeanUtils.toBean(list, DeptRespVO.class));
    }

    @GetMapping(value = {"/list-all-simple", "/simple-list"})
    @Operation(summary = "获取部门精简信息列表", description = "只包含被开启的部门，主要用于前端的下拉选项")
    public CommonResult<List<DeptSimpleRespVO>> getSimpleDeptList() {
        List<Dept> list = deptService.getDeptList(new DeptListReqVO().setStatus(CommonStatusEnum.ENABLE.getStatus()));
        return success(BeanUtils.toBean(list, DeptSimpleRespVO.class));
    }

    @GetMapping("/detail")
    @Operation(summary = "获得部门信息")
    @Parameter(name = "id", description = "编号", required = true, example = "")
    @PreAuthorize("@ss.hasPermission('common:dept:detail')")
    public CommonResult<DeptRespVO> getDept(@RequestParam("id") Long id) {
        Dept dept = deptService.getDept(id);
        return success(BeanUtils.toBean(dept, DeptRespVO.class));
    }

}
