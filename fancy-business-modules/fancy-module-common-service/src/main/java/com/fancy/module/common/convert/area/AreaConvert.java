package com.fancy.module.common.convert.area;


import com.fancy.common.util.collection.CollectionUtils;
import com.fancy.module.common.controller.area.vo.AreaRespVO;
import com.fancy.module.common.controller.user.vo.user.UserRespVO;
import com.fancy.module.common.repository.pojo.area.Area;
import com.fancy.module.common.repository.pojo.dept.Dept;
import com.fancy.module.common.repository.pojo.user.User;
import java.util.List;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author paven
 */
@Mapper
public interface AreaConvert {

    AreaConvert INSTANCE = Mappers.getMapper(AreaConvert.class);

    AreaRespVO convert(Area bean);

    default List<AreaRespVO> convertList(List<Area> list){
        return CollectionUtils.convertList(list, this::convert);
    }
}
