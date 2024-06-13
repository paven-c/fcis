package com.fancy.module.common.service.area;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fancy.module.common.controller.area.vo.AreaListReqVO;
import com.fancy.module.common.repository.pojo.area.Area;
import java.util.List;

/**
 * @author Yanyi
 */
public interface AreaService extends IService<Area> {

    /**
     * 获取区域列表
     *
     * @param reqVO 请求参数
     * @return 区域列表
     */
    List<Area> getAreaList(AreaListReqVO reqVO);
}
