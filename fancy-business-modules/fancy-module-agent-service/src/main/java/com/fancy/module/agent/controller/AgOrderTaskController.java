package com.fancy.module.agent.controller;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.event.SyncReadListener;
import com.alibaba.fastjson.JSONObject;
import com.fancy.common.pojo.CommonResult;
import com.fancy.common.pojo.PageResult;
import com.fancy.module.agent.api.task.dto.OrderTaskListDTO;
import com.fancy.module.agent.controller.req.AgOrderTaskImportReq;
import com.fancy.module.agent.controller.vo.AgUploadTaskReqVO;
import com.fancy.module.agent.controller.vo.OrderTaskListVO;
import com.fancy.module.agent.service.AgOrderTaskService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
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
    public CommonResult<AgUploadTaskReqVO> csvUploadTask(@RequestParam("file") MultipartFile file) {
        // 文件非空判断
        if (file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename.contains(".xlsx")) {
            try {
                SyncReadListener syncReadListener = new SyncReadListener();
                EasyExcel.read(file.getInputStream(), AgOrderTaskImportReq.class, syncReadListener).sheet().doRead();
                List<Object> list = syncReadListener.getList();
                List<AgOrderTaskImportReq> convert = Convert.convert(new TypeReference<>() {
                }, list);
                log.info("导入任务:{}", JSONObject.toJSONString(convert));
                return CommonResult.success(agOrderTaskService.csvUploadTask(convert));
            } catch (Exception e) {
                log.info("csvUploadTask,读取数据失败：{}", e.getMessage(), e);
            }
        }
        return CommonResult.error(500, "上传文件失败，请上传execl文件");
    }

}
