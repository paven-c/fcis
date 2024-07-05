package com.paven.module.compliance.controller.form;

import static com.paven.common.pojo.CommonResult.success;
import static com.paven.component.web.core.util.WebFrameworkUtils.getLoginUserId;

import com.paven.common.pojo.CommonResult;
import com.paven.module.compliance.controller.form.vo.FormFieldCreateReqVO;
import com.paven.module.compliance.controller.form.vo.FormFieldSaveReqVO;
import com.paven.module.compliance.service.FormFieldService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/form-field")
public class FormFieldController {

    @Resource
    private FormFieldService buildFieldService;

    @PostMapping("/create")
    public CommonResult<Boolean> create(@RequestBody @Valid FormFieldCreateReqVO reqVO) {
        return success(buildFieldService.create(getLoginUserId(), reqVO));
    }

    @PutMapping("/save")
    public CommonResult<Boolean> save(@RequestBody @Valid FormFieldSaveReqVO reqVO) {
        return success(buildFieldService.save(getLoginUserId(), reqVO));
    }

    @DeleteMapping("/{id}")
    public CommonResult<Boolean> delete(@PathVariable("id") Long id) {
        return success(buildFieldService.removeById(id));
    }

}
