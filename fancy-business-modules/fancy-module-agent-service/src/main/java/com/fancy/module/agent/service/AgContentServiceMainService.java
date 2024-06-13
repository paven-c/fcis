package com.fancy.module.agent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fancy.module.agent.api.task.dto.ContentServiceMainListDTO;
import com.fancy.module.agent.controller.vo.ContentServiceMainListVo;
import com.fancy.module.agent.repository.pojo.AgContentServiceMain;
import java.util.List;

/**
 * <p>
 * 内容服务主表 服务类
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
public interface AgContentServiceMainService extends IService<AgContentServiceMain> {


    List<ContentServiceMainListVo> listMain(ContentServiceMainListDTO contentServiceMainListDTO);
}
