package com.xuecheng.content.model.dto;

import com.xuecheng.base.execption.ValidationGroups;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author rosemaryxxxxx
 * @Date 2023/10/30 15:30
 * @PackageName:com.xuecheng.content.model.dto
 * @ClassName: EditCourseDto
 * @Description: 修改课程提交的数据模型
 * @Version 1.0
 */
@Data
@ApiModel(value = "EditCourseDto", description = "修改课程基本信息")
public class EditCourseDto extends AddCourseDto{
    @ApiModelProperty(value = "课程id", required = true)
//    @NotEmpty(message = "修改课程信息，id不能为空", groups = {ValidationGroups.Update.class})
    private Long id;
}
