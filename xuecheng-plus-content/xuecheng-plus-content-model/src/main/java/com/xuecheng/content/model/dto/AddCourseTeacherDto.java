package com.xuecheng.content.model.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xuecheng.base.execption.ValidationGroups;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * @Author rosemaryxxxxx
 * @Date 2023/11/3 17:41
 * @PackageName:com.xuecheng.content.model.dto
 * @ClassName: AddCourseTeacherDto
 * @Description: TODO
 * @Version 1.0
 */
@Data
@ApiModel(value="AddCourseTeacherDto", description="新增课程教师基本信息")
public class AddCourseTeacherDto {
    //@NotEmpty(message = "课程名称不能为空")
//    @NotEmpty(message = "新增课程名称不能为空",groups = {ValidationGroups.Inster.class})
//    @NotEmpty(message = "修改课程名称不能为空",groups = {ValidationGroups.Update.class})
//    @ApiModelProperty(value = "课程名称", required = true)
//    private String name;

    /**
     * 新增时为空， 修改时不为空
     */
    private Long id;

    /**
     * 课程标识
     */
    @NotNull(message = "课程id不能为null")
    private Long courseId;

    /**
     * 教师标识
     */
    @NotEmpty(message = "教师姓名不能为null")
    private String teacherName;

    /**
     * 教师职位
     */
    @NotEmpty(message = "教师职位不能为null")
    private String position;

    /**
     * 教师简介
     */
    @NotEmpty(message = "教师介绍不能为null")
    @Size(message = "教师介绍过少",min = 10)
    private String introduction;

    /**
     * 照片
     */
    private String photograph;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;

}
