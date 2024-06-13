package com.fancy.module.agent.controller;


import com.fancy.common.pojo.CommonResult;
import com.fancy.module.agent.api.task.dto.ContentServiceMainListDTO;
import com.fancy.module.agent.api.task.dto.MerchantTaskNumListDTO;
import com.fancy.module.agent.controller.vo.ContentServiceMainListVo;
import com.fancy.module.agent.controller.vo.MerchantTaskNumListVO;
import com.fancy.module.agent.service.AgContentServiceMainService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 内容服务主表 前端控制器
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@RestController
@RequestMapping("/agContentServiceMain")
public class AgContentServiceMainController {

    @Resource
    private AgContentServiceMainService agContentServiceMainService;

    @Operation(summary = "列表关联查询")
    @PostMapping("/listMain")
    public CommonResult<List<ContentServiceMainListVo>> listMain(@RequestBody ContentServiceMainListDTO contentServiceMainListDTO) {
        return CommonResult.success(agContentServiceMainService.listMain(contentServiceMainListDTO));
    }

}
