package com.fancy.module.common.service.dict;

import static com.fancy.common.exception.util.ServiceExceptionUtil.exception;
import static com.fancy.module.common.enums.ErrorCodeConstants.DICT_TYPE_HAS_CHILDREN;
import static com.fancy.module.common.enums.ErrorCodeConstants.DICT_TYPE_NAME_DUPLICATE;
import static com.fancy.module.common.enums.ErrorCodeConstants.DICT_TYPE_NOT_EXISTS;
import static com.fancy.module.common.enums.ErrorCodeConstants.DICT_TYPE_TYPE_DUPLICATE;

import cn.hutool.core.util.StrUtil;
import com.fancy.common.pojo.PageResult;
import com.fancy.common.util.date.LocalDateTimeUtils;
import com.fancy.common.util.object.BeanUtils;
import com.fancy.module.common.controller.admin.dict.vo.type.DictTypePageReqVO;
import com.fancy.module.common.controller.admin.dict.vo.type.DictTypeSaveReqVO;
import com.fancy.module.common.repository.mapper.dict.DictTypeMapper;
import com.fancy.module.common.repository.pojo.dict.DictType;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 字典类型 Service 实现类
 *
 * @author paven
 */
@Service
public class DictTypeServiceImpl implements DictTypeService {

    @Resource
    @Lazy
    private DictDataService dictDataService;

    @Resource
    private DictTypeMapper dictTypeMapper;

    @Override
    public PageResult<DictType> getDictTypePage(DictTypePageReqVO pageReqVO) {
        return dictTypeMapper.selectPage(pageReqVO);
    }

    @Override
    public DictType getDictType(Long id) {
        return dictTypeMapper.selectById(id);
    }

    @Override
    public DictType getDictType(String type) {
        return dictTypeMapper.selectByType(type);
    }

    @Override
    public Long createDictType(DictTypeSaveReqVO createReqVO) {
        // 校验字典类型的名字的唯一性
        validateDictTypeNameUnique(null, createReqVO.getName());
        // 校验字典类型的类型的唯一性
        validateDictTypeUnique(null, createReqVO.getType());

        // 插入字典类型
        DictType dictType = BeanUtils.toBean(createReqVO, DictType.class);
        dictType.setDeletedTime(LocalDateTimeUtils.EMPTY); // 唯一索引，避免 null 值
        dictTypeMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public void updateDictType(DictTypeSaveReqVO updateReqVO) {
        // 校验自己存在
        validateDictTypeExists(updateReqVO.getId());
        // 校验字典类型的名字的唯一性
        validateDictTypeNameUnique(updateReqVO.getId(), updateReqVO.getName());
        // 校验字典类型的类型的唯一性
        validateDictTypeUnique(updateReqVO.getId(), updateReqVO.getType());

        // 更新字典类型
        DictType updateObj = BeanUtils.toBean(updateReqVO, DictType.class);
        dictTypeMapper.updateById(updateObj);
    }

    @Override
    public void deleteDictType(Long id) {
        // 校验是否存在
        DictType dictType = validateDictTypeExists(id);
        // 校验是否有字典数据
        if (dictDataService.getDictDataCountByDictType(dictType.getType()) > 0) {
            throw exception(DICT_TYPE_HAS_CHILDREN);
        }
        // 删除字典类型
        dictTypeMapper.updateToDelete(id, LocalDateTime.now());
    }

    @Override
    public List<DictType> getDictTypeList() {
        return dictTypeMapper.selectList();
    }

    @VisibleForTesting
    void validateDictTypeNameUnique(Long id, String name) {
        DictType dictType = dictTypeMapper.selectByName(name);
        if (dictType == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典类型
        if (id == null) {
            throw exception(DICT_TYPE_NAME_DUPLICATE);
        }
        if (!dictType.getId().equals(id)) {
            throw exception(DICT_TYPE_NAME_DUPLICATE);
        }
    }

    @VisibleForTesting
    void validateDictTypeUnique(Long id, String type) {
        if (StrUtil.isEmpty(type)) {
            return;
        }
        DictType dictType = dictTypeMapper.selectByType(type);
        if (dictType == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典类型
        if (id == null) {
            throw exception(DICT_TYPE_TYPE_DUPLICATE);
        }
        if (!dictType.getId().equals(id)) {
            throw exception(DICT_TYPE_TYPE_DUPLICATE);
        }
    }

    @VisibleForTesting
    DictType validateDictTypeExists(Long id) {
        if (id == null) {
            return null;
        }
        DictType dictType = dictTypeMapper.selectById(id);
        if (dictType == null) {
            throw exception(DICT_TYPE_NOT_EXISTS);
        }
        return dictType;
    }

}
