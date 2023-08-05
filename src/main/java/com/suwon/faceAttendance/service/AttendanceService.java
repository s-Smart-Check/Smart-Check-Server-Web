package com.suwon.faceAttendance.service;

import com.suwon.faceAttendance.domain.Attendance;
import com.suwon.faceAttendance.domain.Registration;
import com.suwon.faceAttendance.repository.AttendanceRepository;
import com.suwon.faceAttendance.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final RegistrationRepository registrationRepository;

    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository, RegistrationRepository registrationRepository) {
        this.attendanceRepository = attendanceRepository;
        this.registrationRepository = registrationRepository;
    }

    public void saveAttendance(Long classId){
        List<Registration> attInfos = registrationRepository.findByClassId(classId);
        List<Attendance> attendances = new ArrayList<>();
        LocalDate now = LocalDate.now();

        for (Registration attInfo : attInfos) {

            Optional<Attendance> confirmAtt = attendanceRepository.findByAttendanceDateAndStudentNumAndClassId(now, attInfo.getStudentNum(), attInfo.getClassId());
            Attendance atd = new Attendance();
            if (confirmAtt.isPresent()){
                atd = confirmAtt.get();
            }

            atd.setStudentName(attInfo.getStudentName());
            atd.setStudentNum(attInfo.getStudentNum());
            atd.setClassId(attInfo.getClassId());
            atd.setClassName(attInfo.getClassName());
            atd.setAttendance(attInfo.getAttendance());
            atd.setAttendanceDate(now);
            atd.setProfessorName(attInfo.getProfessorName());

            attendances.add(atd);
        }

        attendanceRepository.saveAll(attendances);
    }

    public void changeAttendStatus(Attendance attendance){
        attendanceRepository.save(attendance);
    }

    public List<Attendance> getStudentsAttendances(LocalDate atdDate, Long classId){
        return attendanceRepository.findByAttendanceDateAndClassId(atdDate,classId);
    }

    //나중에 수업코드추가
    public Attendance getStudentAttendance(LocalDate atdDate, String studentNum){
        Attendance atdInfo = attendanceRepository
                .findByAttendanceDateAndStudentNum(atdDate, studentNum)
                .orElse(new Attendance(studentNum,"출결정보없음" ,atdDate));

        return atdInfo;
    }

}
