package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author rosemaryxxxxx
 * @Date 2023/10/31 16:26
 * @PackageName:com.xuecheng.content.model.dto
 * @ClassName: TeachplanDto
 * @Description: 课程计划模型类
 * @Version 1.0
 */
@Data
@ToString
public class TeachplanDto extends Teachplan {
    //课程计划关联的媒资信息
    TeachplanMedia teachplanMedia;

    //子节dian
    List<TeachplanDto> teachPlanTreeNodes;
}
