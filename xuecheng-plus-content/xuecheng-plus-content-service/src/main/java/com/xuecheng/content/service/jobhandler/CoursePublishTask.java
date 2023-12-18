package com.xuecheng.content.service.jobhandler;

import com.xuecheng.base.execption.XueChengPlusException;
import com.xuecheng.content.feignclient.SearchServiceClient;
import com.xuecheng.content.mapper.CoursePublishMapper;
import com.xuecheng.content.model.dto.CourseIndex;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.content.service.CoursePublishService;
import com.xuecheng.messagesdk.model.po.MqMessage;
import com.xuecheng.messagesdk.service.MessageProcessAbstract;
import com.xuecheng.messagesdk.service.MqMessageService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * @Author rosemaryxxxxx
 * @Date 2023/12/4 14:59
 * @PackageName:com.xuecheng.content.service.jobhandler
 * @ClassName: CoursePublishTask
 * @Description: 课程发布任务类
 * @Version 1.0
 */
@Slf4j
@Component
public class CoursePublishTask extends MessageProcessAbstract {

    @Autowired
    CoursePublishService coursePublishService;
    @Autowired
    SearchServiceClient searchServiceClient;
    @Autowired
    CoursePublishMapper coursePublishMapper;

    /**
     *  任务调度入口
     *  分片广播任务
     */
    @XxlJob("CoursePublishJobHandler")
    public void coursePublishJobHandler() throws Exception {
        // 分片参数
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();
        log.debug("shardIndex="+shardIndex+",shardTotal="+shardTotal);

        //调用抽象类的方法来执行任务
        //参数:分片序号、分片总数、消息类型、一次最多取到的任务数量、一次任务调度执行的超时时间
        process(shardIndex,shardTotal,"course_publish",30,60);
    }


    //执行发布任务的逻辑
    @Override
    public boolean execute(MqMessage mqMessage) {
        //从mqMessage拿到课程id
        Long courseId = Long.valueOf(mqMessage.getBusinessKey1());

        //课程静态化上传到minio
        generateCourseHtml(mqMessage,courseId);

        //向redis写缓存
        saveCourseCache(mqMessage,courseId);

        //向es写索引
        saveCourseIndex(mqMessage,courseId);

        //返回true任务完成
        return true;

    }

    //stage_one: 生成课程静态化页面并上传至文件系统
    public void generateCourseHtml(MqMessage mqMessage,long courseId){
        log.debug("开始进行课程静态化,课程id:{}",courseId);
        //消息id
        Long taskId = mqMessage.getId();
        MqMessageService mqMessageService = this.getMqMessageService();

        //做任务幂等性处理
        //查询数据库，取出该阶段的执行状态
        int stageOne = mqMessageService.getStageOne(taskId);
        if(stageOne > 0){
            log.debug("课程静态化任务完成，无需处理，课程id：{}", courseId);
            return;
        }
        //开始进行课程静态化,生成html
        File htmlFile =  coursePublishService.generateCourseHtml(courseId);
        //将生成的html上传到minio
        if(htmlFile == null){
            XueChengPlusException.cast("生成的静态页面为空！");
        }
        coursePublishService.uploadCourseHtml(courseId, htmlFile);




        //保存第一阶段状态，在mq表中将stage_one字段的值设置成1
        mqMessageService.completedStageOne(taskId);
    }

    //stage_two: 将课程信息缓存至redis
    public void saveCourseCache(MqMessage mqMessage,long courseId){
        log.debug("将课程信息缓存至redis,课程id:{}",courseId);
        //消息id
        Long taskId = mqMessage.getId();
        MqMessageService mqMessageService = this.getMqMessageService();

        //做任务幂等性处理
        //查询数据库，取出该阶段的执行状态
        int stageTwo = mqMessageService.getStageTwo(taskId);
        if(stageTwo > 0){
            log.debug("课程信息已缓存至redis，无需处理，课程id：{}", courseId);
            return;
        }
        //开始进行将课程信息缓存至redis


        //保存第二阶段状态，在mq表中将stage_two字段的值设置成1
        mqMessageService.completedStageTwo(taskId);
    }

    //stage_three: 保存课程索引信息
    public void saveCourseIndex(MqMessage mqMessage,long courseId){
        log.debug("保存课程索引信息,课程id:{}",courseId);
        //消息id
        Long taskId = mqMessage.getId();
        MqMessageService mqMessageService = this.getMqMessageService();

        //做任务幂等性处理
        //查询数据库，取出该阶段的执行状态
        int stageThree = mqMessageService.getStageThree(taskId);
        if(stageThree > 0){
            log.debug("课程信息已经同步索引到es，无需处理，课程id：{}", courseId);
            return;
        }
        //开始保存课程索引信息
        Boolean result = saveCourseIndex(courseId);
        if(result){
            //保存第三阶段状态，在mq表中将stage_three字段的值设置成1
            mqMessageService.completedStageThree(taskId);
        }

    }

    /**
     * 同步课程信息到索引
     * @param courseId 课程id
     * @return
     */
    private Boolean saveCourseIndex(Long courseId){

        //取出课程发布消息
        CoursePublish coursePulish = coursePublishMapper.selectById(courseId);
        CourseIndex courseIndex = new CourseIndex();
        BeanUtils.copyProperties(coursePulish,courseIndex);
        //调用远程服务api添加课程信息到索引
        Boolean add = searchServiceClient.add(courseIndex);
        if(!add){
            XueChengPlusException.cast("添加索引失败！");
        }
        return add;

    }










}
