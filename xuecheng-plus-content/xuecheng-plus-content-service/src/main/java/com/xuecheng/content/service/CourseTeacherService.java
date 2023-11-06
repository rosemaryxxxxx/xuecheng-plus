package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.AddCourseTeacherDto;
import com.xuecheng.content.model.po.CourseTeacher;

import java.util.List;

/**
 * @Author rosemaryxxxxx
 * @Date 2023/11/3 15:00
 * @PackageName:com.xuecheng.content.service.impl
 * @ClassName: CourseTeacherService
 * @Description: 师资管理接口
 * @Version 1.0
 */
public interface CourseTeacherService {

    /**
     * 根据课程id查询课程教师信息
     * @param courseId
     * @return
     */
    public List<CourseTeacher> getCourseTeacher(Long courseId);

    /**
     * 修改或者添加课程教师信息
     * @param companyId
     * @param dto
     * @return
     */
    public CourseTeacher addCourseTeacher(Long companyId, AddCourseTeacherDto dto);

    /**
     * 删除课程教师
     * @param companyId
     * @param courseId
     * @param teahcerId
     */
    public void deleteCourseTeacher(Long companyId, Long courseId, Long teahcerId);
}
