package com.paven.module.common.service.dept;


import static com.paven.common.exception.util.ServiceExceptionUtil.exception;
import static com.paven.common.util.collection.CollectionUtils.convertSet;
import static com.paven.module.common.enums.ErrorCodeConstants.DEPT_EXITS_CHILDREN;
import static com.paven.module.common.enums.ErrorCodeConstants.DEPT_NAME_DUPLICATE;
import static com.paven.module.common.enums.ErrorCodeConstants.DEPT_NOT_ENABLE;
import static com.paven.module.common.enums.ErrorCodeConstants.DEPT_NOT_FOUND;
import static com.paven.module.common.enums.ErrorCodeConstants.DEPT_PARENT_ERROR;
import static com.paven.module.common.enums.ErrorCodeConstants.DEPT_PARENT_IS_CHILD;
import static com.paven.module.common.enums.ErrorCodeConstants.DEPT_PARENT_NOT_EXITS;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.paven.common.enums.CommonStatusEnum;
import com.paven.common.util.object.BeanUtils;
import com.paven.component.datapermission.core.annotation.DataPermission;
import com.paven.module.common.controller.dept.vo.DeptListReqVO;
import com.paven.module.common.controller.dept.vo.DeptSaveReqVO;
import com.paven.module.common.repository.cache.redis.RedisKeyConstants;
import com.paven.module.common.repository.mapper.dept.DeptMapper;
import com.paven.module.common.repository.pojo.dept.Dept;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 部门 Service 实现类
 *
 * @author paven
 */
@Service
@Validated
@Slf4j
public class DeptServiceImpl implements DeptService {

    @Resource
    private DeptMapper deptMapper;

    @Override
    @CacheEvict(cacheNames = RedisKeyConstants.DEPT_CHILDREN_ID_LIST, allEntries = true)
    public Long createDept(DeptSaveReqVO createReqVO) {
        if (createReqVO.getParentId() == null) {
            createReqVO.setParentId(Dept.PARENT_ID_ROOT);
        }
        // 校验父部门的有效性
        validateParentDept(null, createReqVO.getParentId());
        // 校验部门名的唯一性
        validateDeptNameUnique(null, createReqVO.getParentId(), createReqVO.getName());
        // 新增部门
        Dept dept = Dept.builder().deptName(createReqVO.getName()).parentId(createReqVO.getParentId()).status(createReqVO.getStatus()).build();
        deptMapper.insert(dept);
        return dept.getId();
    }

    @Override
    @CacheEvict(cacheNames = RedisKeyConstants.DEPT_CHILDREN_ID_LIST, allEntries = true)
    public void updateDept(DeptSaveReqVO updateReqVO) {
        // 校验自己存在
        validateDeptExists(updateReqVO.getId());
        // 校验父部门的有效性
        validateParentDept(updateReqVO.getId(), updateReqVO.getParentId());
        // 校验部门名的唯一性
        validateDeptNameUnique(updateReqVO.getId(), updateReqVO.getParentId(), updateReqVO.getName());
        // 更新部门
        Dept updateObj = BeanUtils.toBean(updateReqVO, Dept.class);
        updateObj.setDeptName(updateReqVO.getName());
        deptMapper.updateById(updateObj);
    }

    @Override
    @CacheEvict(cacheNames = RedisKeyConstants.DEPT_CHILDREN_ID_LIST, allEntries = true)
    public void deleteDept(Long id) {
        // 校验是否存在
        validateDeptExists(id);
        // 校验是否有子部门
        if (deptMapper.selectCountByParentId(id) > 0) {
            throw exception(DEPT_EXITS_CHILDREN);
        }
        // 删除部门
        deptMapper.deleteById(id);
    }

    @VisibleForTesting
    void validateDeptExists(Long id) {
        if (id == null) {
            return;
        }
        Dept dept = deptMapper.selectById(id);
        if (dept == null) {
            throw exception(DEPT_NOT_FOUND);
        }
    }

    @VisibleForTesting
    void validateParentDept(Long id, Long parentId) {
        if (parentId == null || Dept.PARENT_ID_ROOT.equals(parentId)) {
            return;
        }
        // 不能设置自己为父部门
        if (Objects.equals(id, parentId)) {
            throw exception(DEPT_PARENT_ERROR);
        }
        // 父部门不存在
        Dept parentDept = deptMapper.selectById(parentId);
        if (parentDept == null) {
            throw exception(DEPT_PARENT_NOT_EXITS);
        }
        // 递归校验父部门，如果父部门是自己的子部门，则报错，避免形成环路
        if (id == null) {
            return;
        }
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            // 校验环路
            parentId = parentDept.getParentId();
            if (Objects.equals(id, parentId)) {
                throw exception(DEPT_PARENT_IS_CHILD);
            }
            // 继续递归下一级父部门
            if (parentId == null || Dept.PARENT_ID_ROOT.equals(parentId)) {
                break;
            }
            parentDept = deptMapper.selectById(parentId);
            if (parentDept == null) {
                break;
            }
        }
    }

    @VisibleForTesting
    void validateDeptNameUnique(Long id, Long parentId, String name) {
        Dept dept = deptMapper.selectByParentIdAndName(parentId, name);
        if (dept == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的部门
        if (id == null) {
            throw exception(DEPT_NAME_DUPLICATE);
        }
        if (ObjectUtil.notEqual(dept.getId(), id)) {
            throw exception(DEPT_NAME_DUPLICATE);
        }
    }

    @Override
    public Dept getDept(Long id) {
        return deptMapper.selectById(id);
    }

    @Override
    public List<Dept> getDeptList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return deptMapper.selectBatchIds(ids);
    }

    @Override
    public List<Dept> getDeptList(DeptListReqVO reqVO) {
        List<Dept> list = deptMapper.selectList(reqVO);
        list.sort(Comparator.comparing(Dept::getSort));
        return list;
    }

    @Override
    public List<Dept> getChildDeptList(Long id) {
        List<Dept> children = new LinkedList<>();
        // 遍历每一层
        Collection<Long> parentIds = Collections.singleton(id);
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            // 查询当前层，所有的子部门
            List<Dept> depts = deptMapper.selectListByParentId(parentIds);
            // 1. 如果没有子部门，则结束遍历
            if (CollUtil.isEmpty(depts)) {
                break;
            }
            // 2. 如果有子部门，继续遍历
            children.addAll(depts);
            parentIds = convertSet(depts, Dept::getId);
        }
        return children;
    }

    @Override
    @DataPermission(enable = false)
    @Cacheable(cacheNames = RedisKeyConstants.DEPT_CHILDREN_ID_LIST, key = "#id")
    public Set<Long> getChildDeptIdListFromCache(Long id) {
        List<Dept> children = getChildDeptList(id);
        return convertSet(children, Dept::getId);
    }

    @Override
    public void validateDeptList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 获得科室信息
        Map<Long, Dept> deptMap = getDeptMap(ids);
        // 校验
        ids.forEach(id -> {
            Dept dept = deptMap.get(id);
            if (dept == null) {
                throw exception(DEPT_NOT_FOUND);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(dept.getStatus())) {
                throw exception(DEPT_NOT_ENABLE, dept.getDeptName());
            }
        });
    }

}
