package com.xuecheng.content.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author rosemaryxxxxx
 * @Date 2023/11/22 15:48
 * @PackageName:com.xuecheng.content.model.dto
 * @ClassName: BindTeachplanMediaDto
 * @Description: 绑定课程和计划的模型类
 * @Version 1.0
 */
@Data
@ApiModel(value="BindTeachplanMediaDto", description="教学计划-媒资绑定提交数据")
public class BindTeachplanMediaDto {

    @ApiModelProperty(value = "媒资文件id", required = true)
    private String mediaId;

    @ApiModelProperty(value = "媒资文件名称", required = true)
    private String fileName;

    @ApiModelProperty(value = "课程计划标识", required = true)
    private Long teachplanId;
}
