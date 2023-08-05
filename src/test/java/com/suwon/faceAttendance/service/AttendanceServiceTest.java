package com.suwon.faceAttendance.service;

import com.suwon.faceAttendance.domain.ClassManager;
import com.suwon.faceAttendance.domain.Registration;
import com.suwon.faceAttendance.repository.RegistrationRepository;
import com.suwon.faceAttendance.repository.ClassManagerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@SpringBootTest
@Transactional
public class AttendanceServiceTest {

    @Autowired
    RegistrationRepository attendanceRepository;
    @Autowired
    ClassManagerRepository classManagerRepository;
    @Autowired
    RegistrationService attendanceService;

    @Test
    public void 출석상태변경(){
        //given
        Registration student = new Registration();
        student.setId(1L);
        student.setStudentName("홍길동");
        student.setAttendance("결석");
        attendanceRepository.save(student);
        Registration student2 = attendanceService.findStudent("123").get();
        student2.setAttendance("출석");

        ClassManager classManager = new ClassManager();
        classManager.setClassName("테스트");
        classManager.setStartTime(LocalTime.now());
        classManagerRepository.save(classManager);

        //when
        attendanceService.modifyAttendance(student2,1L);

        //then
        Registration result = attendanceRepository.findById(1L).get();
        Assertions.assertThat(student.getAttendance()).isNotEqualTo(result.getAttendance());

    }

    @Test
    public void 지각여부체크(){
        //given
        Registration student = new Registration();
        student.setId(1L);
        student.setStudentName("홍길동");
        student.setAttendance("출석");

        ClassManager classManager = new ClassManager();
        classManager.setClassName("테스트");
        classManager.setStartTime(LocalTime.now().minusHours(1));
        classManagerRepository.save(classManager);

        //when
        attendanceService.modifyAttendance(student,1L);

        //then
        Registration result = attendanceRepository.findById(1L).get();
        Assertions.assertThat(result.getAttendance()).isEqualTo("지각");
    }

    @Test
    public void findAll(){
        //given
        Registration student1 = new Registration();
        student1.setId(1L);
        student1.setStudentName("홍길동");
        student1.setAttendance("결석");
        attendanceRepository.save(student1);

        Registration student2 = new Registration();
        student2.setId(2L);
        student2.setStudentName("가나다");
        student2.setAttendance("출석");
        attendanceRepository.save(student2);

        //when
        List<Registration> students = attendanceService.findStudents();

        //then
        Assertions.assertThat(students.size()).isEqualTo(2);
    }
}
