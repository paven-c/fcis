package com.paven.module.compliance.controller.form;

import static com.paven.common.pojo.CommonResult.success;

import com.paven.common.pojo.CommonResult;
import com.paven.common.pojo.PageResult;
import com.paven.module.compliance.controller.form.vo.FormCheckReqVO;
import com.paven.module.compliance.controller.form.vo.FormCreateReqVO;
import com.paven.module.compliance.controller.form.vo.FormPageReqVO;
import com.paven.module.compliance.controller.form.vo.FormRespVO;
import com.paven.module.compliance.controller.form.vo.FormRuleSaveReqVO;
import com.paven.module.compliance.controller.form.vo.FormUpdateReqVO;
import com.paven.module.compliance.service.FormService;
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
@RequestMapping("/form")
public class FormController {

    @Resource
    private FormService formService;

    @GetMapping("/page")
    public CommonResult<PageResult<FormRespVO>> page(@Valid FormPageReqVO reqVO) {
        return success(formService.getFormPage(reqVO));
    }

    @GetMapping("/list")
    public CommonResult<List<FormRespVO>> list(@Valid FormPageReqVO reqVO) {
        return success(formService.getFormList(reqVO));
    }

    @GetMapping("/{id}")
    public CommonResult<FormRespVO> detail(@PathVariable("id") Long id) {
        return success(formService.getFormDetail(id));
    }

    @PostMapping("/check")
    public CommonResult<Boolean> check(@RequestBody @Valid FormCheckReqVO reqVO) {
        return success(formService.check(reqVO));
    }

    @PutMapping("/rule/save")
    public CommonResult<Boolean> saveRule(@RequestBody @Valid FormRuleSaveReqVO reqVO) {
        return success(formService.updateRule(reqVO));
    }

    @PostMapping("/create")
    public CommonResult<Long> create(@RequestBody @Valid FormCreateReqVO reqVO) {
        return success(formService.create(reqVO));
    }

    @PutMapping("/update")
    public CommonResult<Boolean> update(@RequestBody @Valid FormUpdateReqVO reqVO) {
        return success(formService.update(reqVO));
    }

    @DeleteMapping("/{id}")
    public CommonResult<Boolean> delete(@PathVariable("id") Long id) {
        return success(formService.delete(id));
    }

}
