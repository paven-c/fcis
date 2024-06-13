package com.fancy.module.agent.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fancy.module.agent.api.task.dto.ContentServiceMainListDTO;
import com.fancy.module.agent.controller.vo.ContentServiceMainListVo;
import com.fancy.module.agent.repository.pojo.AgContentServiceMain;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 内容服务主表 Mapper 接口
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@Mapper
public interface AgContentServiceMainMapper extends BaseMapper<AgContentServiceMain> {

    List<ContentServiceMainListVo> listMain(@Param("contentServiceMainListDTO") ContentServiceMainListDTO contentServiceMainListDTO);
}
