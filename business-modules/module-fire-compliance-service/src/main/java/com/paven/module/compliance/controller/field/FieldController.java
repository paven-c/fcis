package com.paven.module.compliance.controller.field;

import static com.paven.common.pojo.CommonResult.success;
import static com.paven.component.web.core.util.WebFrameworkUtils.getLoginUserId;

import com.paven.common.pojo.CommonResult;
import com.paven.common.pojo.PageResult;
import com.paven.module.compliance.controller.field.vo.FieldCreateReqVO;
import com.paven.module.compliance.controller.field.vo.FieldPageReqVO;
import com.paven.module.compliance.controller.field.vo.FieldRespVO;
import com.paven.module.compliance.controller.field.vo.FieldUpdateReqVO;
import com.paven.module.compliance.service.FieldService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yanyi
 * @since 2024-07-01
 */

@RestController
@RequestMapping("/field")
public class FieldController {

    @Resource
    private FieldService fieldService;

    @GetMapping("/page")
    public CommonResult<PageResult<FieldRespVO>> page(@Valid FieldPageReqVO reqVO) {
        return success(fieldService.fieldPage(reqVO));
    }

    @GetMapping("/list")
    public CommonResult<List<FieldRespVO>> list(@Valid FieldPageReqVO reqVO) {
        return success(fieldService.fieldList(reqVO));
    }

    @GetMapping("/{id}")
    public CommonResult<FieldRespVO> detail(@PathVariable("id") Long id) {
        return success(fieldService.detail(id));
    }

    @PostMapping("/create")
    public CommonResult<Long> create(@RequestBody @Valid FieldCreateReqVO reqVO) {
        return success(fieldService.create(getLoginUserId(), reqVO));
    }

    @PutMapping("/update")
    public CommonResult<Boolean> update(@RequestBody @Valid FieldUpdateReqVO reqVO) {
        return success(fieldService.update(getLoginUserId(), reqVO));
    }

    @DeleteMapping("/{id}")
    public CommonResult<Boolean> delete(@PathVariable("id") Long id) {
        return success(fieldService.delete(id));
    }

}
