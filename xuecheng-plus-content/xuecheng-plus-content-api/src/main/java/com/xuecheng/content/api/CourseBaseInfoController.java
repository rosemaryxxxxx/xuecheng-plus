package com.xuecheng.content.api;

import com.xuecheng.base.modle.PageParams;
import com.xuecheng.base.modle.PageResult;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description 课程信息编辑接口
 * @author Mr.M
 * @date 2022/9/6 11:29
 * @version 1.0
 */
@RestController
public class CourseBaseInfoController {

    @RequestMapping("/course/list")
    //请求体是json，要转成对象，使用 @RequestBody
    public PageResult<CourseBase> list(PageParams pageParams, @RequestBody QueryCourseParamsDto queryCourseParamsDto){

        return null;
    }
}
