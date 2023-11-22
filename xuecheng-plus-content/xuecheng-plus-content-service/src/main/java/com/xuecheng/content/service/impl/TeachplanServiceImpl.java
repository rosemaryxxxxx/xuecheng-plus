package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.execption.CommonError;
import com.xuecheng.base.execption.XueChengPlusException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.service.TeachplanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * @Author rosemaryxxxxx
 * @Date 2023/11/1 10:38
 * @PackageName:com.xuecheng.content.service.impl
 * @ClassName: TeachplanServiceImpl
 * @Description: TODO
 * @Version 1.0
 */
@Service
public class TeachplanServiceImpl implements TeachplanService {

    @Autowired
    TeachplanMapper teachplanMapper;

    @Autowired
    TeachplanMediaMapper teachplanMediaMapper;

    @Override
    public List<TeachplanDto> findTeachplanTree(Long courseId) {
        return teachplanMapper.selectTreeNodes(courseId);
    }

    @Transactional
    @Override
    public void saveTeachplan(SaveTeachplanDto saveTeachplanDto) {
        //课程计划id
        Long id = saveTeachplanDto.getId();
        if(id != null){
            //修改课程计划
            Teachplan teachplan = teachplanMapper.selectById(id);
            BeanUtils.copyProperties(saveTeachplanDto,teachplan);
            teachplanMapper.updateById(teachplan);
        }else {
            //新增课程计划
            //获取最新排序号
            int count = getTeachplanMaxOrderby(saveTeachplanDto.getCourseId(), saveTeachplanDto.getParentid());
            Teachplan teachplanNew = new Teachplan();
            BeanUtils.copyProperties(saveTeachplanDto, teachplanNew);
            //设置新增课程的排序号
            teachplanNew.setOrderby(count+1);
            teachplanMapper.insert(teachplanNew);
        }
    }

    @Transactional
    @Override
    public void deleteTeachplan(Long teachplanId) {
        //获取要删除的Teachplan
        Teachplan teachplanDelete = teachplanMapper.selectById(teachplanId);
        Integer grade = teachplanDelete.getGrade();
        if(grade == 1){
            //删除第一级别的章节
            //判断其第二级别的章节数是否为0
            LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Teachplan::getParentid,teachplanId);
            Integer count = teachplanMapper.selectCount(queryWrapper);
            if(count != 0){
                XueChengPlusException.cast(CommonError.DELETE_ERROR);
            }else {
                teachplanMapper.deleteById(teachplanId);
            }

        }else if(grade == 2){
            //删除第二级别的章节
            //同时删除 课程计划-媒资 信息，媒资的信息还没有删除，后续可以交给媒资信息管理模块，做异步删除
            LambdaQueryWrapper<TeachplanMedia> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TeachplanMedia::getTeachplanId, teachplanId);
            //媒资信息和课程计划一对一
            TeachplanMedia teachplanMediaDelete = teachplanMediaMapper.selectOne(queryWrapper);
            if(teachplanMediaDelete == null){
                teachplanMapper.deleteById(teachplanId);
            }else {
                teachplanMediaMapper.deleteById(teachplanMediaDelete.getId());
                teachplanMapper.deleteById(teachplanId);
            }
        }else {
            XueChengPlusException.cast("120400", "别用你的技术挑战我的耐性");
        }
        //格式化orderby字段
        getTeachplanMaxOrderby(teachplanDelete.getCourseId(), teachplanDelete.getParentid());

    }

    @Override
    public void moveDownTeachplan(Long teachplanId) {
        Teachplan teachplanMoveDown = teachplanMapper.selectById(teachplanId);
        //判断本teachplan不是最后一个，才能下移
        int teachplanMoveDownorderby = teachplanMoveDown.getOrderby();
        //获取同一课程同一章节最大的orderby
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId, teachplanMoveDown.getCourseId());
        queryWrapper.eq(Teachplan::getParentid, teachplanMoveDown.getParentid());
        List<Teachplan> teachplans = teachplanMapper.selectList(queryWrapper);
        int size = teachplans.size();
        if(teachplanMoveDownorderby >= size){
            XueChengPlusException.cast("0","最后一个课程计划不能下移");
        }else {
            teachplanMoveDown.setOrderby(teachplanMoveDownorderby+1);
            teachplanMapper.updateById(teachplanMoveDown);
            Teachplan teachplanUp = teachplans.get(teachplanMoveDownorderby);
            teachplanUp.setOrderby(teachplanMoveDownorderby);
            teachplanMapper.updateById(teachplanUp);
        }

    }

    @Override
    public void moveUpTeachplan(Long teachplanId) {
        Teachplan teachplanMoveUp = teachplanMapper.selectById(teachplanId);
        //判断本teachplan不是第一个，才能上移
        int teachplanMoveUporderby = teachplanMoveUp.getOrderby();
        //获取同一课程同一章节最小的orderby
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId, teachplanMoveUp.getCourseId());
        queryWrapper.eq(Teachplan::getParentid, teachplanMoveUp.getParentid());
        List<Teachplan> teachplans = teachplanMapper.selectList(queryWrapper);
        int size = teachplans.size();
        if(teachplanMoveUporderby > 1 && teachplanMoveUporderby <= size){
            teachplanMoveUp.setOrderby(teachplanMoveUporderby-1);
            teachplanMapper.updateById(teachplanMoveUp);
            Teachplan teachplanDown = teachplans.get(teachplanMoveUporderby-2);
            teachplanDown.setOrderby(teachplanMoveUporderby);
            teachplanMapper.updateById(teachplanDown);
        }else {
            XueChengPlusException.cast("0","第一个课程计划不能上移");
        }
    }

    @Transactional
    @Override
    public TeachplanMedia associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto) {
        //教学计划id
        Long teachplanId = bindTeachplanMediaDto.getTeachplanId();
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        if(teachplan==null){
            XueChengPlusException.cast("教学计划不存在");
        }
        Integer grade = teachplan.getGrade();
        if(grade!=2){
            XueChengPlusException.cast("只允许第二级教学计划绑定媒资文件");
        }
        //课程id
        Long courseId = teachplan.getCourseId();

        //先删除原来该教学计划绑定的媒资
        teachplanMediaMapper.delete(new LambdaQueryWrapper<TeachplanMedia>().eq(TeachplanMedia::getTeachplanId,teachplanId));

        //再添加教学计划与媒资的绑定关系
        TeachplanMedia teachplanMedia = new TeachplanMedia();
        teachplanMedia.setCourseId(courseId);
        teachplanMedia.setTeachplanId(teachplanId);
        teachplanMedia.setMediaFilename(bindTeachplanMediaDto.getFileName());
        teachplanMedia.setMediaId(bindTeachplanMediaDto.getMediaId());
        teachplanMedia.setCreateDate(LocalDateTime.now());
        teachplanMediaMapper.insert(teachplanMedia);
        return teachplanMedia;
    }

    /**
     * 获取最新的排序号
     * @param courseId 课程id
     * @param perantId 父课程计划id
     * @return int 最新排序号
     */
    private int getTeachplanMaxOrderby(Long courseId, Long perantId){
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId,courseId);
        queryWrapper.eq(Teachplan::getParentid, perantId);
        List<Teachplan> teachplans = teachplanMapper.selectList(queryWrapper);
        //按照orderby字段大小排序
        teachplans.sort(Comparator.comparingInt(Teachplan::getOrderby));
//        for (Teachplan t: teachplans
//             ) {
//            max = Math.max(max,t.getOrderby());
//        }
//        return max;
        int len = teachplans.size();
        for (int i = 0; i < len; i++) {
            teachplans.get(i).setOrderby(i+1);
            teachplanMapper.updateById(teachplans.get(i));
        }
        return len;
    }
}
