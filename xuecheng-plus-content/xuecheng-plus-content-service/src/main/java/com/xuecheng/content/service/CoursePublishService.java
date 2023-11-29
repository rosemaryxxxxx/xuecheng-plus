package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CoursePreviewDto;
import org.springframework.stereotype.Service;

/**
 * @Author rosemaryxxxxx
 * @Date 2023/11/28 20:47
 * @PackageName:com.xuecheng.content.service
 * @ClassName: CoursePublishService
 * @Description: 课程预览、发布接口
 * @Version 1.0
 */
public interface CoursePublishService {
    /**
     * @description 获取课程预览信息
     * @param courseId 课程id
     * @return com.xuecheng.content.model.dto.CoursePreviewDto
     */
    public CoursePreviewDto getCoursePreviewInfo(Long courseId);
}
