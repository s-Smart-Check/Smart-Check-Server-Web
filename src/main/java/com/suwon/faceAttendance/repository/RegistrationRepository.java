package com.suwon.faceAttendance.repository;

import com.suwon.faceAttendance.domain.Attendance;
import com.suwon.faceAttendance.domain.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration,Long> {

    Registration save(Registration registration);

    List<Registration> findAll();

    Optional<Registration> findById(Long id);

    List<Registration> findByClassId(Long classId);
    Optional<Registration> findByStudentNum(String stdId);

    @Query("SELECT s FROM Registration s WHERE s.studentNum NOT IN :attendanceStudentNumbers")
    List<Registration> findAbsentStudents(List<String> attendanceStudentNumbers);

    @Query("SELECT s FROM Registration s WHERE s.studentNum IN :attendanceStudentNumbers")
    List<Registration> findAttendStudents(List<String> attendanceStudentNumbers);
}
