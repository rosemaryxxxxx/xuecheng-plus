package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.AddCourseTeacherDto;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import com.xuecheng.content.service.TeachplanService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author rosemaryxxxxx
 * @Date 2023/11/1 15:57
 * @PackageName:com.xuecheng.content.api
 * @ClassName: CourseTeacherController
 * @Description: 师资管理前端控制器
 * @Version 1.0
 */
@RestController
public class CourseTeacherController {

    @Autowired
    CourseTeacherService courseTeacherService;

    @ApiOperation("查询教师")
    @GetMapping("/courseTeacher/list/{courseId}")
    public List<CourseTeacher> getCourseTeacher(@PathVariable Long courseId){
        return courseTeacherService.getCourseTeacher(courseId);
    }

    @ApiOperation("添加或者更新教师信息")
    @PostMapping("/courseTeacher")
    public CourseTeacher saveorUpdateCourseTeacher(@RequestBody @Validated AddCourseTeacherDto dto){
        Long companyId = 1232141425L;
        return courseTeacherService.addCourseTeacher(companyId,dto);
    }

    @DeleteMapping("/courseTeacher/course/{courseId}/{teacherId}")
    public void deleteCourseTeaacher(@PathVariable Long courseId, @PathVariable Long teacherId){
        Long companyId = 1232141425L;
        courseTeacherService.deleteCourseTeacher(companyId,courseId,teacherId);
    }



}
