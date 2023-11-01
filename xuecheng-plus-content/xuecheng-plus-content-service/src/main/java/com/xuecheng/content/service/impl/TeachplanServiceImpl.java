package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.service.TeachplanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author rosemaryxxxxx
 * @Date 2023/11/1 10:38
 * @PackageName:com.xuecheng.content.service.impl
 * @ClassName: TeachplanServiceImpl
 * @Description: TODO
 * @Version 1.0
 */
@Service
public class TeachplanServiceImpl implements TeachplanService {

    @Autowired
    TeachplanMapper teachplanMapper;

    @Override
    public List<TeachplanDto> findTeachplanTree(Long courseId) {
        return teachplanMapper.selectTreeNodes(courseId);
    }

    @Transactional
    @Override
    public void saveTeachplan(SaveTeachplanDto saveTeachplanDto) {
        //课程计划id
        Long id = saveTeachplanDto.getId();
        if(id != null){
            //修改课程计划
            Teachplan teachplan = teachplanMapper.selectById(id);
            BeanUtils.copyProperties(saveTeachplanDto,teachplan);
            teachplanMapper.updateById(teachplan);
        }else {
            //新增课程计划
            //获取最新排序号
            int count = getTeachplanCount(saveTeachplanDto.getCourseId(), saveTeachplanDto.getParentid());
            Teachplan teachplanNew = new Teachplan();
            BeanUtils.copyProperties(saveTeachplanDto, teachplanNew);
            //设置新增课程的排序号
            teachplanNew.setOrderby(count+1);
            teachplanMapper.insert(teachplanNew);
        }
    }

    /**
     * 获取最新的排序号
     * @param courseId 课程id
     * @param perantId 父课程计划id
     * @return int 最新排序号
     */
    private int getTeachplanCount(Long courseId, Long perantId){
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId,courseId);
        queryWrapper.eq(Teachplan::getParentid, perantId);
        return teachplanMapper.selectCount(queryWrapper);
    }
}
