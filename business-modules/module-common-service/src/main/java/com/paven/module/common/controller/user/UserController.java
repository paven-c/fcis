package com.paven.module.common.controller.user;


import static com.paven.common.pojo.CommonResult.success;
import static com.paven.common.util.collection.CollectionUtils.convertList;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.paven.common.enums.CommonStatusEnum;
import com.paven.common.enums.GenderEnum;
import com.paven.common.pojo.CommonResult;
import com.paven.common.pojo.PageParam;
import com.paven.common.pojo.PageResult;
import com.paven.component.excel.core.util.ExcelUtils;
import com.paven.module.common.controller.user.vo.user.UserImportExcelVO;
import com.paven.module.common.controller.user.vo.user.UserImportRespVO;
import com.paven.module.common.controller.user.vo.user.UserPageReqVO;
import com.paven.module.common.controller.user.vo.user.UserRespVO;
import com.paven.module.common.controller.user.vo.user.UserSaveReqVO;
import com.paven.module.common.controller.user.vo.user.UserSimpleRespVO;
import com.paven.module.common.controller.user.vo.user.UserUpdatePasswordReqVO;
import com.paven.module.common.controller.user.vo.user.UserUpdateReqVO;
import com.paven.module.common.controller.user.vo.user.UserUpdateStatusReqVO;
import com.paven.module.common.convert.user.UserConvert;
import com.paven.module.common.repository.pojo.dept.Dept;
import com.paven.module.common.repository.pojo.permission.UserRole;
import com.paven.module.common.repository.pojo.user.User;
import com.paven.module.common.service.dept.DeptService;
import com.paven.module.common.service.permission.PermissionService;
import com.paven.module.common.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author paven
 */
@Tag(name = "用户")
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private DeptService deptService;
    @Resource
    private PermissionService permissionService;

    @GetMapping("/page")
    @Operation(summary = "获得用户分页列表")
//    @PreAuthorize("@ss.hasRole('super_admin')")
    public CommonResult<PageResult<UserRespVO>> getUserPage(@Valid UserPageReqVO pageReqVO) {
        // 获得用户分页列表
        PageResult<User> pageResult = userService.getUserPage(pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(new PageResult<>(pageResult.getTotalCount(), pageReqVO.getPageNum(), pageResult.getPageSize()));
        }
        Map<Long, Set<Long>> userRoleIdMap = Optional.ofNullable(permissionService.getUserRoleIdListByUserIds(convertList(pageResult.getList(), User::getId)))
                .orElse(Lists.newArrayList())
                .stream().collect(Collectors.groupingBy(UserRole::getUserId, Collectors.mapping(UserRole::getRoleId, Collectors.toSet())));
        // 拼接数据
        Map<Long, Dept> deptMap = deptService.getDeptMap(convertList(pageResult.getList(), User::getDeptId));
        return success(new PageResult<>(UserConvert.INSTANCE.convertList(pageResult.getList(), deptMap, userRoleIdMap), pageResult.getTotalCount(),
                pageResult.getPageNum(),
                pageResult.getPageSize()));
    }

    @GetMapping({"/list-all-simple", "/simple-list"})
    @Operation(summary = "获取用户精简信息列表", description = "只包含被开启的用户，主要用于前端的下拉选项")
    public CommonResult<List<UserSimpleRespVO>> getSimpleUserList() {
        List<User> list = userService.getUserListByStatus(CommonStatusEnum.ENABLE.getStatus());
        // 拼接数据
        Map<Long, Dept> deptMap = deptService.getDeptMap(convertList(list, User::getDeptId));
        return success(UserConvert.INSTANCE.convertSimpleList(list, deptMap));
    }

    @PostMapping("/create")
    @Operation(summary = "新增用户")
//    @PreAuthorize("@ss.hasPermission('common:user:create')")
    public CommonResult<Long> createUser(@Valid @RequestBody UserSaveReqVO reqVO) {
        Long id = userService.createUser(reqVO);
        return success(id);
    }

    @PutMapping("/update")
    @Operation(summary = "修改用户")
//    @PreAuthorize("@ss.hasPermission('common:user:update')")
    public CommonResult<Boolean> updateUser(@Valid @RequestBody UserUpdateReqVO reqVO) {
        userService.updateUser(reqVO);
        return success(true);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
//    @PreAuthorize("@ss.hasPermission('common:user:delete')")
    public CommonResult<Boolean> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return success(true);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获得用户详情")
    @Parameter(name = "id", description = "编号", required = true, example = "")
//    @PreAuthorize("@ss.hasPermission('common:user:detail')")
    public CommonResult<UserRespVO> getUser(@PathVariable("id") Long id) {
        User user = userService.getUser(id);
        if (user == null) {
            return success(null);
        }
        // 拼接数据
//        Dept dept = deptService.getDept(user.getDeptId());
        return success(UserConvert.INSTANCE.convert(user, null, permissionService.getUserRoleIdListByUserId(id)));
    }

    @PutMapping("/update-password")
    @Operation(summary = "重置用户密码")
    @PreAuthorize("@ss.hasPermission('common:user:update-password')")
    public CommonResult<Boolean> updateUserPassword(@Valid @RequestBody UserUpdatePasswordReqVO reqVO) {
        userService.updateUserPassword(reqVO.getId(), reqVO.getPassword());
        return success(true);
    }

    @PutMapping("/update-status")
    @Operation(summary = "修改用户状态")
    @PreAuthorize("@ss.hasPermission('common:user:update')")
    public CommonResult<Boolean> updateUserStatus(@Valid @RequestBody UserUpdateStatusReqVO reqVO) {
        userService.updateUserStatus(reqVO.getId(), reqVO.getStatus());
        return success(true);
    }

    @GetMapping("/export")
    @Operation(summary = "导出用户")
    @PreAuthorize("@ss.hasPermission('common:user:export')")
    public void exportUserList(@Validated UserPageReqVO exportReqVO, HttpServletResponse response) throws IOException {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<User> list = userService.getUserPage(exportReqVO).getList();
        // 输出 Excel
        Map<Long, Dept> deptMap = deptService.getDeptMap(convertList(list, User::getDeptId));
        ExcelUtils.write(response, "用户数据.xls", "数据", UserRespVO.class, UserConvert.INSTANCE.convertList(list, deptMap, null));
    }

    @GetMapping("/get-import-template")
    @Operation(summary = "获得导入用户模板")
    public void importTemplate(HttpServletResponse response) throws IOException {
        // 手动创建导出 demo
        List<UserImportExcelVO> list = Arrays.asList(
                UserImportExcelVO.builder().username("").deptId(1L).email("").mobile("")
                        .nickname("").status(CommonStatusEnum.ENABLE.getStatus()).gender(GenderEnum.MALE.getCode()).build(),
                UserImportExcelVO.builder().username("").deptId(2L).email("").mobile("")
                        .nickname("").status(CommonStatusEnum.DISABLE.getStatus()).gender(GenderEnum.FEMALE.getCode()).build()
        );
        // 输出
        ExcelUtils.write(response, "用户导入模板.xls", "用户列表", UserImportExcelVO.class, list);
    }

    @PostMapping("/import")
    @Operation(summary = "导入用户")
    @Parameters({
            @Parameter(name = "file", description = "Excel 文件", required = true),
            @Parameter(name = "updateSupport", description = "是否支持更新，默认为 false", example = "true")
    })
    @PreAuthorize("@ss.hasPermission('common:user:import')")
    public CommonResult<UserImportRespVO> importExcel(@RequestParam("file") MultipartFile file,
            @RequestParam(value = "updateSupport", required = false, defaultValue = "false") Boolean updateSupport) throws Exception {
        List<UserImportExcelVO> list = ExcelUtils.read(file, UserImportExcelVO.class);
        return success(userService.importUserList(list, updateSupport));
    }

}
