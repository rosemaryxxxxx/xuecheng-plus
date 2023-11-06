package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.execption.XueChengPlusException;
import com.xuecheng.base.modle.PageParams;
import com.xuecheng.base.modle.PageResult;
import com.xuecheng.content.mapper.*;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.*;
import com.xuecheng.content.service.CourseBaseInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {

    @Autowired
    CourseBaseMapper courseBaseMapper;

    @Autowired
    CourseMarketMapper courseMarketMapper;

    @Autowired
    CourseCategoryMapper courseCategoryMapper;

    @Autowired
    TeachplanMapper teachplanMapper;

    @Autowired
    CourseTeacherMapper courseTeacherMapper;

    @Autowired
    TeachplanMediaMapper teachplanMediaMapper;


    /**
     * 课程信息分页查询实现
     * @param pageParams 分页参数
     * @param queryCourseParamsDto 查询条件
     * @return
     */
    @Override
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto) {

        //查询条件插入到sql中
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(queryCourseParamsDto.getCourseName()),CourseBase::getName,queryCourseParamsDto.getCourseName());
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParamsDto.getAuditStatus()),CourseBase::getAuditStatus,queryCourseParamsDto.getAuditStatus());
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParamsDto.getPublishStatus()),CourseBase::getStatus,queryCourseParamsDto.getPublishStatus());

        //分页参数
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(),pageParams.getPageSize());
        Page<CourseBase> pageResult =  courseBaseMapper.selectPage(page,queryWrapper);

        //准备返回PageResult格式的结果
        //数据
        List<CourseBase> items = pageResult.getRecords();
        //总记录数
        long counts = pageResult.getTotal();

        PageResult<CourseBase> courseBasePageResult = new PageResult<>(items,counts,pageParams.getPageNo(),pageParams.getPageSize());
        System.out.println(courseBasePageResult);

        return courseBasePageResult;
    }

    /**
     * 创建课程基本信息表
     * @param companyId 机构id
     * @param dto 课程信息
     * @return
     */
    @Transactional
    @Override
    public CourseBaseInfoDto creatCourseBase(Long companyId, AddCourseDto dto) {

        //参数的合法性校验
        //使用JSR303在控制器层校验之后，业务层就不用校验参数的合法性
        //Contoller中校验请求参数的合法性，包括：必填项校验，数据格式校验，比如：是否是符合一定的日期格式，等。
        //Service中要校验的是业务规则相关的内容，比如：课程已经审核通过所以提交失败。
//        if (StringUtils.isBlank(dto.getName())) {
//            throw new XueChengPlusException("课程名称为空");
//        }
//
//        if (StringUtils.isBlank(dto.getMt())) {
//            throw new XueChengPlusException("课程分类为空");
//        }
//
//        if (StringUtils.isBlank(dto.getSt())) {
//            throw new XueChengPlusException("课程分类为空");
//        }
//
//        if (StringUtils.isBlank(dto.getGrade())) {
//            throw new XueChengPlusException("课程等级为空");
//        }
//
//        if (StringUtils.isBlank(dto.getTeachmode())) {
//            throw new XueChengPlusException("教育模式为空");
//        }
//
//        if (StringUtils.isBlank(dto.getUsers())) {
//            throw new XueChengPlusException("适应人群为空");
//        }
//
//        if (StringUtils.isBlank(dto.getCharge())) {
//            throw new XueChengPlusException("收费规则为空");
//        }

        //向课程基本信息表course_base写数据
        //新增对象
        CourseBase courseBaseNew = new CourseBase();
        //一个一个向courseBaseNew中写属性,很繁琐
//        courseBaseNew.setName(dto.getName());
//        courseBaseNew.setMt(dto.getMt());
//        ...
        //使用BeanUtils来添加属性
        BeanUtils.copyProperties(dto,courseBaseNew);
        courseBaseNew.setCompanyId(companyId);
        //设置审核状态
        courseBaseNew.setAuditStatus("20202");
        //设置发布状态,初始为未发布
        courseBaseNew.setStatus("203001");
        courseBaseNew.setCreateDate(LocalDateTime.now());
        //插入课程基本信息表
        int insert = courseBaseMapper.insert(courseBaseNew);
        if(insert<=0){
            throw new XueChengPlusException("1","新增课程信息失败");
        }

        //向课程营销信息表course_market写数据
        CourseMarket courseMarketNew = new CourseMarket();
        //主键,只要courseBaseNew插入成功就有id了
        Long courseId = courseBaseNew.getId();
        BeanUtils.copyProperties(dto,courseMarketNew);
        courseMarketNew.setId(courseId);
        int i = saveCourseMarket(courseMarketNew);
        if(i<=0){
            throw new XueChengPlusException("1","保存课程营销信息失败");
        }
        //查询课程基本信息以及营销信息并返回
        return getCourseBaseInfo(courseId);
    }

    //保存课程营销信息
    //不直接使用courseMarketMapper.updateById，因为在更新之前需要做一些校验
    private int saveCourseMarket(CourseMarket courseMarketNew){
        //收费规则
        String charge = courseMarketNew.getCharge();
        if(StringUtils.isBlank(charge)){
            throw new XueChengPlusException("1","收费规则没有选择");
        }
        //收费规则为收费
        if(charge.equals("201001")){
            if(courseMarketNew.getPrice() == null || courseMarketNew.getPrice().floatValue()<=0){
                throw new XueChengPlusException("1","课程为收费价格不能为空且必须大于0");
            }
        }
        //根据id从课程营销表查询
        CourseMarket courseMarketObj = courseMarketMapper.selectById(courseMarketNew.getId());
        if(courseMarketObj == null){
            return courseMarketMapper.insert(courseMarketNew);
        }else{
            BeanUtils.copyProperties(courseMarketNew,courseMarketObj);
            courseMarketObj.setId(courseMarketNew.getId());
            return courseMarketMapper.updateById(courseMarketObj);
        }
    }

    //根据课程id查询课程基本信息，包括基本信息和营销信息
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId){

        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(courseBase == null){
            return null;
        }
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(courseBase,courseBaseInfoDto);
        if(courseMarket != null){
            BeanUtils.copyProperties(courseMarket,courseBaseInfoDto);
        }

        //查询分类名称
        CourseCategory courseCategoryBySt = courseCategoryMapper.selectById(courseBase.getSt());
        courseBaseInfoDto.setStName(courseCategoryBySt.getName());
        CourseCategory courseCategoryByMt = courseCategoryMapper.selectById(courseBase.getMt());
        courseBaseInfoDto.setMtName(courseCategoryByMt.getName());

        return courseBaseInfoDto;

    }


    /**
     * 更新课程信息
     * @param companyId  机构id
     * @param dto  修改课程信息dto
     * @return
     */
    @Transactional
    @Override
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto dto) {
        //机构id
        Long courseId = dto.getId();

        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(courseBase == null){
            XueChengPlusException.cast("1","课程不存在");
        }

        //校验本机构只能修改本机构的课程
        if(!courseBase.getCompanyId().equals(companyId)){
            XueChengPlusException.cast("1","只能修改本机构的课程");
        }

        //封装基本信息的数据
        BeanUtils.copyProperties(dto,courseBase);
        courseBase.setChangeDate(LocalDateTime.now());

        //更新课程基本信息
        int i = courseBaseMapper.updateById(courseBase);
        if(i<=0){
            XueChengPlusException.cast("1","课程基本信息更新失败");
        }

        //封装营销信息的数据
//        CourseMarket courseMarket = courseMarketMapper.selectById(courseBase);
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(dto,courseMarket);
        saveCourseMarket(courseMarket);

        //查询课程信息
        CourseBaseInfoDto courseBaseInfo = getCourseBaseInfo(courseId);
        return courseBaseInfo;

    }

    @Transactional
    @Override
    public void deleteCourseBase(Long courseId) {
        //删除课程需要删除课程相关的基本信息、营销信息、课程计划、课程教师信息。
        //课程的审核状态为未提交时方可删除。
        CourseBase courseBaseDelete = courseBaseMapper.selectById(courseId);
        String status = courseBaseDelete.getStatus();
        if(!Objects.equals(status, "203001")){
            XueChengPlusException.cast("1","请删除未提交状态的课程！");
        }
        //删除courseBase
        int i = courseBaseMapper.deleteById(courseId);
        if(i<=0){
            XueChengPlusException.cast("1","删除课程基本信息失败");
        }

        //删除营销信息
        //营销信息表的主键为课程id
        int i1 = courseMarketMapper.deleteById(courseId);
        if(i1<=0){
            XueChengPlusException.cast("1","删除课程营销信息失败");
        }

        //删除课程计划
        LambdaQueryWrapper<Teachplan> teachplanLambdaQueryWrapper = new LambdaQueryWrapper<>();
        teachplanLambdaQueryWrapper.eq(Teachplan::getCourseId, courseId);
        int i2 = teachplanMapper.delete(teachplanLambdaQueryWrapper);
        if(i2<=0){
            XueChengPlusException.cast("1","删除课程计划信息失败");
        }

        //删除计划-媒资信息
        LambdaQueryWrapper<TeachplanMedia> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeachplanMedia::getCourseId, courseId);
        int i4 = teachplanMediaMapper.delete(queryWrapper);
        if(i4<=0){
            XueChengPlusException.cast("1","删除课程计划-媒资信息失败");
        }

        //删除课程教师信息
        LambdaQueryWrapper<CourseTeacher> courseTeacherLambdaQueryWrapper = new LambdaQueryWrapper<>();
        courseTeacherLambdaQueryWrapper.eq(CourseTeacher::getCourseId, courseId);
        int i3 = courseTeacherMapper.delete(courseTeacherLambdaQueryWrapper);
        if(i3<=0){
            XueChengPlusException.cast("1","删除课程教师信息失败");
        }

    }


}
