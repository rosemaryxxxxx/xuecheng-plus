package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.execption.XueChengPlusException;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.dto.AddCourseTeacherDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import com.xuecheng.content.service.TeachplanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @Author rosemaryxxxxx
 * @Date 2023/11/3 15:02
 * @PackageName:com.xuecheng.content.service.impl
 * @ClassName: CourseTeacherServiceImpl
 * @Description: TODO
 * @Version 1.0
 */
@Service
public class CourseTeacherServiceImpl implements CourseTeacherService {

    @Autowired
    CourseTeacherMapper courseTeacherMapper;

    @Autowired
    CourseBaseMapper courseBaseMapper;

    @Override
    public List<CourseTeacher> getCourseTeacher(Long courseId) {
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacher::getCourseId, courseId);
        return courseTeacherMapper.selectList(queryWrapper);
    }

    @Transactional
    @Override
    public CourseTeacher addCourseTeacher(Long companyId, AddCourseTeacherDto dto) {
        //只允许向机构自己的课程中添加老师、删除老师。需要验证
        Long courseId = dto.getCourseId();
        Long companyId1 = courseBaseMapper.selectById(courseId).getCompanyId();
        CourseTeacher courseTeacherNew = new CourseTeacher();
        if(Objects.equals(companyId1, companyId)){
            Long id = dto.getId();
            BeanUtils.copyProperties(dto,courseTeacherNew);
            if(id == null){
                //新增
                courseTeacherNew.setCreateDate(LocalDateTime.now());
                int insert = courseTeacherMapper.insert(courseTeacherNew);
                if(insert<=0){
                    XueChengPlusException.cast("1","教师信息新增失败！");
                }
            }else {
                //修改
                int update = courseTeacherMapper.updateById(courseTeacherNew);
                if(update<=0){
                    XueChengPlusException.cast("1","教师信息更新失败！");
                }
            }
        }else {
            XueChengPlusException.cast("1","只允许向机构自己的课程中添加老师、删除老师！");
        }
        return courseTeacherNew;
    }

    @Transactional
    @Override
    public void deleteCourseTeacher(Long companyId, Long courseId, Long teahcerId) {
        //只允许向机构自己的课程中添加老师、删除老师。需要验证
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        Long companyId1 = courseBase.getCompanyId();
        if(Objects.equals(companyId1, companyId)){
            //删除
            int i = courseTeacherMapper.deleteById(teahcerId);
            if(i<=0){
                XueChengPlusException.cast("1","教授信息删除失败！");
            }
        }else {
            XueChengPlusException.cast("1","只允许向机构自己的课程中添加老师、删除老师！");
        }
    }
}
