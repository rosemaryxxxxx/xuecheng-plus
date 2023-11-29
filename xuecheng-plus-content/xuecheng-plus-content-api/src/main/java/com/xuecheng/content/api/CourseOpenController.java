package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.service.CourseBaseInfoService;
import com.xuecheng.content.service.CoursePublishService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author rosemaryxxxxx
 * @Date 2023/11/29 14:19
 * @PackageName:com.xuecheng.content.api
 * @ClassName: CourseOpenController
 * @Description: 请求路径content/open/ 不需要登录
 * @Version 1.0
 */
@Api(value = "课程公开查询接口",tags = "课程公开查询接口")
@RestController
@RequestMapping("/open")
public class CourseOpenController {
    @Autowired
    private CourseBaseInfoService courseBaseInfoService;

    @Autowired
    private CoursePublishService coursePublishService;


    @GetMapping("/course/whole/{courseId}")
    public CoursePreviewDto getPreviewInfo(@PathVariable("courseId") Long courseId) {
        //获取课程预览信息
        CoursePreviewDto coursePreviewInfo = coursePublishService.getCoursePreviewInfo(courseId);
        return coursePreviewInfo;
    }
}
