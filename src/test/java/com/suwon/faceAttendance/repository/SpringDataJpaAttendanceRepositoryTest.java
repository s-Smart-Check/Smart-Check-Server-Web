package com.suwon.faceAttendance.repository;

import com.suwon.faceAttendance.domain.Attendance;
import com.suwon.faceAttendance.domain.Registration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class SpringDataJpaAttendanceRepositoryTest {

    @Autowired
    RegistrationRepository attendanceRepository;

    @Test
    public void findAll(){
        //given
        Registration student1 = new Registration();
        student1.setId(1234567L);
        student1.setAttendance("결석");
        student1.setStudentName("홍길동");
        attendanceRepository.save(student1);

        Registration student2 = new Registration();
        student2.setId(1234568L);
        student2.setAttendance("미결");
        student2.setStudentName("홍길동");
        attendanceRepository.save(student2);

        //when
        List<Registration> students = attendanceRepository.findAll();

        //then
        Assertions.assertThat(students.size()).isEqualTo(2);
    }
}
