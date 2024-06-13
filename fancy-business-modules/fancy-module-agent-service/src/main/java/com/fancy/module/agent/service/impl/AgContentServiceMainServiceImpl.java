package com.fancy.module.agent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.module.agent.api.task.dto.ContentServiceMainListDTO;
import com.fancy.module.agent.controller.vo.ContentServiceMainListVo;
import com.fancy.module.agent.repository.mapper.AgContentServiceMainMapper;
import com.fancy.module.agent.repository.pojo.AgContentServiceMain;
import com.fancy.module.agent.service.AgContentServiceMainService;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 内容服务主表 服务实现类
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@Service
public class AgContentServiceMainServiceImpl extends ServiceImpl<AgContentServiceMainMapper, AgContentServiceMain> implements AgContentServiceMainService {

    @Override
    public List<ContentServiceMainListVo> listMain(ContentServiceMainListDTO contentServiceMainListDTO) {
        if (Objects.isNull(contentServiceMainListDTO)) {
            contentServiceMainListDTO = new ContentServiceMainListDTO();
        }
        return this.baseMapper.listMain(contentServiceMainListDTO);
    }

}
