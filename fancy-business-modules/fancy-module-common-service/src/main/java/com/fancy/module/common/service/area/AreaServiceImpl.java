package com.fancy.module.common.service.area;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.common.enums.DeleteStatusEnum;
import com.fancy.module.common.controller.area.vo.AreaListReqVO;
import com.fancy.module.common.repository.mapper.area.AreaMapper;
import com.fancy.module.common.repository.pojo.area.Area;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author Yanyi
 */
@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {

    @Resource
    private AreaMapper areaMapper;

    @Override
    public List<Area> getAreaList(AreaListReqVO reqVO) {
        return areaMapper.selectList(Area::getParentId, reqVO.getParentAreaId(), Area::getDeleted, DeleteStatusEnum.ACTIVATED.getStatus());
    }
}
