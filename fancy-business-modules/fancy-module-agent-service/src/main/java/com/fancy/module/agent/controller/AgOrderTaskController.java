package com.fancy.module.agent.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.fancy.common.enums.DeleteStatusEnum;
import com.fancy.common.pojo.CommonResult;
import com.fancy.common.pojo.PageResult;
import com.fancy.module.agent.api.task.dto.OrderTaskListDTO;
import com.fancy.module.agent.controller.req.AgOrderTaskImportReq;
import com.fancy.module.agent.controller.vo.AgUploadTaskReqVO;
import com.fancy.module.agent.controller.vo.OrderTaskListVO;
import com.fancy.module.agent.service.AgOrderTaskService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 订单任务表 前端控制器
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@Slf4j
@RestController
@RequestMapping("/agOrderTask")
public class AgOrderTaskController {

    @Resource
    private AgOrderTaskService agOrderTaskService;

    @PostMapping("/listOrderTask")
    public CommonResult<PageResult<OrderTaskListVO>> listOrderTask(@RequestBody OrderTaskListDTO orderTaskListDTO) {
        return CommonResult.success(agOrderTaskService.listOrderTask(orderTaskListDTO));
    }

    @PostMapping("/csv/upload/task")
    @Operation(summary = "导入任务")
    public CommonResult<List<AgUploadTaskReqVO>> csvUploadTask(@RequestParam("file") MultipartFile file) {
        // 文件非空判断
        if (file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }
        if (!"text/csv".equals(file.getContentType())) {
            throw new RuntimeException("只支持上传CSV文件");
        }
        try {
            Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CsvToBean<AgOrderTaskImportReq> csvToBean = new CsvToBeanBuilder<AgOrderTaskImportReq>(reader)
                    .withType(AgOrderTaskImportReq.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreEmptyLine(true)
                    .build();
            List<AgOrderTaskImportReq> agOrderTaskImportReqList = csvToBean.parse();
            if (CollectionUtil.isNotEmpty(agOrderTaskImportReqList)) {
                return CommonResult.success(agOrderTaskService.csvUploadTask(agOrderTaskImportReqList));
            }
            return CommonResult.success(null);
        } catch (Exception ex) {
            log.error("导入任务失败", ex);
            throw new RuntimeException("导入任务失败");
        }
    }

}
