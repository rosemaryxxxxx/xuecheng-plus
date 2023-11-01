package com.xuecheng.content;

import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.model.dto.TeachplanDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Author rosemaryxxxxx
 * @Date 2023/11/1 10:24
 * @PackageName:com.xuecheng.content
 * @ClassName: TeachplanMapperTest
 * @Description: 课程计划mapper测试
 * @Version 1.0
 */
@SpringBootTest
public class TeachplanMapperTest {

    @Autowired
    TeachplanMapper teachplanMapper;

    @Test
    public void testselectTreeNodes(){
        List<TeachplanDto> list = teachplanMapper.selectTreeNodes(117L);
        System.out.println(list);
    }
}

