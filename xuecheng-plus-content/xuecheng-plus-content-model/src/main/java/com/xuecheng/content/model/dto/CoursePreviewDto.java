package com.xuecheng.content.model.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author rosemaryxxxxx
 * @Date 2023/11/28 19:49
 * @PackageName:com.xuecheng.content.model.dto
 * @ClassName: CoursePreviewDto
 * @Description: 课程预览数据模型
 * @Version 1.0
 */
@Data
@ToString
public class CoursePreviewDto {

    //课程基本信息,课程营销信息
    CourseBaseInfoDto courseBase;


    //课程计划信息
    List<TeachplanDto> teachplans;

    //师资信息暂时不加...


}
