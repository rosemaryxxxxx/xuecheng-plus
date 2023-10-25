package com.xuecheng.content;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategroyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CourseCategroyServiceTests {
    @Autowired
    CourseCategroyService courseCategroyService;

    @Test
    void testCourseCategroyService(){
        List<CourseCategoryTreeDto> list = courseCategroyService.queryTreeNodes("1");
    }

}
