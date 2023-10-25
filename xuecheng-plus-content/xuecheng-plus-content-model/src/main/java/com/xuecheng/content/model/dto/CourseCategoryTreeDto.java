package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.CourseCategory;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description 课程分类树型结点dto
 * @author Mr.M
 * @date 2022/9/7 15:16
 * @version 1.0
 */
@Data
public class CourseCategoryTreeDto extends CourseCategory implements Serializable {

    //继承了CourseCategory，只需要加一个属性，子节点的list
    List<CourseCategoryTreeDto> childrenTreeNodes;
}
