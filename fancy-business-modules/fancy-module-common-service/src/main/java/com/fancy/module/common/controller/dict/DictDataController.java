package com.fancy.module.common.controller.dict;

import static com.fancy.common.pojo.CommonResult.success;

import com.fancy.common.enums.CommonStatusEnum;
import com.fancy.common.pojo.CommonResult;
import com.fancy.common.pojo.PageParam;
import com.fancy.common.pojo.PageResult;
import com.fancy.common.util.object.BeanUtils;
import com.fancy.component.core.util.ExcelUtils;
import com.fancy.module.common.controller.dict.vo.data.DictDataPageReqVO;
import com.fancy.module.common.controller.dict.vo.data.DictDataRespVO;
import com.fancy.module.common.controller.dict.vo.data.DictDataSaveReqVO;
import com.fancy.module.common.controller.dict.vo.data.DictDataSimpleRespVO;
import com.fancy.module.common.repository.pojo.dict.DictData;
import com.fancy.module.common.service.dict.DictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
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

@Tag(name = "字典数据")
@RestController
@RequestMapping("/system/dict-data")
@Validated
public class DictDataController {

    @Resource
    private DictDataService dictDataService;

    @PostMapping("/create")
    @Operation(summary = "新增字典数据")
    @PreAuthorize("@ss.hasPermission('common:dict:create')")
    public CommonResult<Long> createDictData(@Valid @RequestBody DictDataSaveReqVO createReqVO) {
        Long dictDataId = dictDataService.createDictData(createReqVO);
        return success(dictDataId);
    }

    @PutMapping("/update")
    @Operation(summary = "修改字典数据")
    @PreAuthorize("@ss.hasPermission('common:dict:update')")
    public CommonResult<Boolean> updateDictData(@Valid @RequestBody DictDataSaveReqVO updateReqVO) {
        dictDataService.updateDictData(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除字典数据")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('common:dict:delete')")
    public CommonResult<Boolean> deleteDictData(Long id) {
        dictDataService.deleteDictData(id);
        return success(true);
    }

    @GetMapping(value = {"/list-all-simple", "simple-list"})
    @Operation(summary = "获得全部字典数据列表", description = "一般用于管理后台缓存字典数据在本地")
    // 无需添加权限认证，因为前端全局都需要
    public CommonResult<List<DictDataSimpleRespVO>> getSimpleDictDataList() {
        List<DictData> list = dictDataService.getDictDataList(
                CommonStatusEnum.ENABLE.getStatus(), null);
        return success(BeanUtils.toBean(list, DictDataSimpleRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "/获得字典类型的分页列表")
    @PreAuthorize("@ss.hasPermission('common:dict:query')")
    public CommonResult<PageResult<DictDataRespVO>> getDictTypePage(@Valid DictDataPageReqVO pageReqVO) {
        PageResult<DictData> pageResult = dictDataService.getDictDataPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DictDataRespVO.class));
    }

    @GetMapping(value = "/get")
    @Operation(summary = "/查询字典数据详细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('common:dict:query')")
    public CommonResult<DictDataRespVO> getDictData(@RequestParam("id") Long id) {
        DictData dictData = dictDataService.getDictData(id);
        return success(BeanUtils.toBean(dictData, DictDataRespVO.class));
    }

    @GetMapping("/export")
    @Operation(summary = "导出字典数据")
    @PreAuthorize("@ss.hasPermission('common:dict:export')")
    public void export(HttpServletResponse response, @Valid DictDataPageReqVO exportReqVO) throws IOException {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DictData> list = dictDataService.getDictDataPage(exportReqVO).getList();
        // 输出
        ExcelUtils.write(response, "字典数据.xls", "数据", DictDataRespVO.class,
                BeanUtils.toBean(list, DictDataRespVO.class));
    }

}
