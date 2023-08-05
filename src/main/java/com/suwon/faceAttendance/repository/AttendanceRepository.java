package com.suwon.faceAttendance.repository;

import com.suwon.faceAttendance.domain.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    List<Attendance> findByAttendanceDateAndClassId(LocalDate date,Long classId);
    Optional<Attendance> findByAttendanceDateAndStudentNumAndClassId(LocalDate date, String stdId,Long classId);
    Optional<Attendance> findByAttendanceDateAndStudentNum(LocalDate date, String stdId);
}
