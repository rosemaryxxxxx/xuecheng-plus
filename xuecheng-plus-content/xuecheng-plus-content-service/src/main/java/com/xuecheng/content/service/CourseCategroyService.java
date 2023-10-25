package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;

import java.util.List;

public interface CourseCategroyService {
    /**
     * 课程分类树型结构查询
     */
    public List<CourseCategoryTreeDto> queryTreeNodes(String id);
}
