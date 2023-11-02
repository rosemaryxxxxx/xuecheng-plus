package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;

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
}
