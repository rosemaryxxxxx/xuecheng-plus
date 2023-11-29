package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.TeachplanMedia;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @Author rosemaryxxxxx
 * @Date 2023/11/1 10:34
 * @PackageName:com.xuecheng.content.service
 * @ClassName: TeachplanService
 * @Description: 课程计划相关接口
 * @Version 1.0
 */
public interface TeachplanService {

    /**
     * 查询课程计划树型结构
     * @param courseId
     * @return
     */
    public List<TeachplanDto> findTeachplanTree(Long courseId);

    /**
     * 保存课程计划
     * @param saveTeachplanDto 课程计划信息
     */
    public void saveTeachplan(SaveTeachplanDto saveTeachplanDto);

    /**
     * 按id删除教学计划
     * @param teachplanId
     */
    public void deleteTeachplan(Long teachplanId);

    /**
     * 下移课程计划
     * @param teachplanId
     */
    public void moveDownTeachplan(Long teachplanId);


    /**
     * 上移课程计划
     * @param teachplanId
     */
    public void moveUpTeachplan(Long teachplanId);

    /**
     * @description 教学计划绑定媒资
     * @param bindTeachplanMediaDto
     * @return com.xuecheng.content.model.po.TeachplanMedia
     */
    public TeachplanMedia associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto);

    /**
     * 删除教学计划和媒资信息的绑定
     * @param teachPlanId
     * @param mediaId
     */
    public void deleteTeachplanMedia(String teachPlanId , String mediaId);

}
