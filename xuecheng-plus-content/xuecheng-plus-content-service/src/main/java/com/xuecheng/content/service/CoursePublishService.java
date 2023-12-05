package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CoursePreviewDto;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;

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

    /**a
     * 提交审核
     * @param companyId
     * @param courseId
     */
    public void commitAudit(Long companyId, Long courseId);

    /**
     * 课程发布
     * @param courseId
     */
    void coursepublish(Long companyId,Long courseId);

    /**
     * @description 课程静态化
     * @param courseId  课程id
     * @return File 静态化文件
     */
    public File generateCourseHtml(Long courseId);

    /**
     * @description 上传课程静态化页面
     * @param file  静态化文件
     * @return void
     */
    public void  uploadCourseHtml(Long courseId,File file);

}
