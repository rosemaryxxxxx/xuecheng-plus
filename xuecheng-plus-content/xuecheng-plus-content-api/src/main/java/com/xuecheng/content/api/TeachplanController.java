package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.service.TeachplanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author rosemaryxxxxx
 * @Date 2023/10/31 16:32
 * @PackageName:com.xuecheng.content.api
 * @ClassName: TeachplanController
 * @Description: 课程计划编辑接口
 * @Version 1.0
 */
@Api(value = "课程计划编辑接口",tags = "课程计划编辑接口")
@RestController
public class TeachplanController {

    @Autowired
    TeachplanService teachplanService;

    @ApiOperation("查询课程计划树形结构")
    @ApiImplicitParam(value = "courseId",name = "课程Id",required = true,dataType = "Long",paramType = "path")
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(@PathVariable Long courseId){
        return teachplanService.findTeachplanTree(courseId);
    }

    @ApiOperation("课程计划创建或修改")
    @PostMapping("/teachplan")
    public void saveTeachplan(@RequestBody SaveTeachplanDto teachplan){
        teachplanService.saveTeachplan(teachplan);
    }


    @ApiOperation("按照教学计划id删除教学计划")
    @DeleteMapping("/teachplan/{teachplanId}")
    public void deleteTeachplan(@PathVariable Long teachplanId) {
        teachplanService.deleteTeachplan(teachplanId);
    }

    @ApiOperation("示将课程计划向下移动")
    @PostMapping("teachplan/movedown/{teachplanId}")
    public void moveDownTeachplan(@PathVariable Long teachplanId){
        teachplanService.moveDownTeachplan(teachplanId);
    }

    @ApiOperation("将课程计划向上移动")
    @PostMapping("teachplan/moveup/{teachplanId}")
    public void moveUpTeachplan(@PathVariable Long teachplanId){
        teachplanService.moveUpTeachplan(teachplanId);
    }

    @ApiOperation(value = "课程计划和媒资信息绑定")
    @PostMapping("/teachplan/association/media")
    public void associationMedia(@RequestBody BindTeachplanMediaDto bindTeachplanMediaDto){
        teachplanService.associationMedia(bindTeachplanMediaDto);
    }

    @DeleteMapping("/teachplan/association/media/{teachPlanId}/{mediaId}")
    public void deleteTeachplanMedia(@PathVariable String teachPlanId ,@PathVariable String mediaId){
        teachplanService.deleteTeachplanMedia(teachPlanId,mediaId);
    }


}
