package com.fancy.module.common.controller.area;

import static com.fancy.common.pojo.CommonResult.success;

import com.fancy.common.pojo.CommonResult;
import com.fancy.module.common.controller.area.vo.AreaListReqVO;
import com.fancy.module.common.controller.area.vo.AreaRespVO;
import com.fancy.module.common.convert.area.AreaConvert;
import com.fancy.module.common.repository.pojo.area.Area;
import com.fancy.module.common.service.area.AreaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yanyi
 */

@RestController
@RequestMapping("/area")
public class AreaController {

    @Resource
    private AreaService areaService;

    @GetMapping("/list")
    @Operation(summary = "列表查询")
    public CommonResult<List<AreaRespVO>> list(@RequestBody AreaListReqVO reqVO) {
        return success(AreaConvert.INSTANCE.convertList(areaService.getAreaList(reqVO)));
    }

    @GetMapping("/detail")
    @Operation(summary = "查看详情")
    public CommonResult<Area> getAgAreaById(Long id) {
        return success(areaService.getById(id));
    }

    @PostMapping("/create")
    @Operation(summary = "新增")
    public CommonResult<Boolean> saveAgArea(Area area) {
        return success(areaService.save(area));
    }

    @PostMapping("/update")
    @Operation(summary = "修改")
    public CommonResult<Boolean> update(Area area) {
        return success(areaService.updateById(area));
    }

    @PostMapping("/delete")
    @Operation(summary = "删除")
    public CommonResult<Boolean> remove(Long id) {
        return success(areaService.removeById(id));
    }

}
