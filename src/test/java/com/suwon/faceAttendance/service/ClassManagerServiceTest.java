package com.suwon.faceAttendance.service;

import com.suwon.faceAttendance.domain.ClassManager;
import com.suwon.faceAttendance.repository.ClassManagerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@SpringBootTest
public class ClassManagerServiceTest {
    @Autowired
    ClassManagerRepository classManagerRepository;
    @Autowired
    ClassManagerService classManagerService;

    @Test
    public void 수업등록(){
        //given
        ClassManager classManager = new ClassManager();
        classManager.setClassName("테스트");

        //when
        classManagerService.createClass(classManager);

        //then
        ClassManager result = classManagerRepository.findByClassName("테스트").get();
        Assertions.assertThat(classManager.getClassName())
                .isEqualTo(result.getClassName());

    }

    @Test
    public void 수업시작시간등록(){
        //given
        LocalTime nowTime = LocalTime.now();
        ClassManager classManager = new ClassManager();
        classManager.setClassName("테스트");
        classManager.setStartTime(nowTime);

        //when
        classManagerService.modifyClassStartTime(classManager);

        //then
        ClassManager result = classManagerRepository.findByClassName("테스트").get();
        Assertions.assertThat(result.getStartTime()).isEqualTo(nowTime);
    }

    @Test
    public void findAll(){
        //given
        ClassManager classManager1 = new ClassManager();
        classManager1.setClassName("테스트1");
        classManagerRepository.save(classManager1);

        ClassManager classManager2 = new ClassManager();
        classManager2.setClassName("테스트2");
        classManagerRepository.save(classManager2);

        //when
        List<ClassManager> classManagers = classManagerService.findAll();

        //then
        Assertions.assertThat(classManagers.size()).isEqualTo(2);
    }
}
