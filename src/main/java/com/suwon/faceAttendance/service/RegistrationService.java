package com.suwon.faceAttendance.service;

import com.suwon.faceAttendance.domain.ClassManager;
import com.suwon.faceAttendance.domain.Registration;
import com.suwon.faceAttendance.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final ClassManagerService classManagerService;


    @Autowired
    public RegistrationService(RegistrationRepository registrationRepository, ClassManagerService classManagerService) {
        this.registrationRepository = registrationRepository;
        this.classManagerService = classManagerService;

    }

    public List<Registration> findStudents(){
        return registrationRepository.findAll();
    }

    public Registration modifyAttendance(Registration student, Long classId){

        student = checkTardy(student, classId);
        return registrationRepository.save(student);
    }

    public void resetAttendance(Long classId){
        List<Registration> registrations = registrationRepository.findByClassId(classId);
        for (Registration registration : registrations) {
            registration.setAttendance("결석");
            registration.setState(0);
            registrationRepository.save(registration);
        }

    }

    public void save(Registration student){

        registrationRepository.save(student);
    }

    public Registration checkTardy(Registration student, Long classId){
        if(student.getAttendance().equals("결석")){
            return student;
        }

        ClassManager classManager = classManagerService.findClassInfo(classId).get();
        LocalTime tardyTime = classManager.getStartTime().plusMinutes(30);
        LocalTime now = LocalTime.now();
        if(tardyTime.isBefore(now)){
            //나중에 지각처리로 변경하기
            student.setAttendance("출석");
        }

        return student;
    }

    public List<Registration> setAttend(List<String> attendanceStudentNums, String attendOrAbsent){
        List<Registration> students;
        if (attendOrAbsent == "결석"){
            students = registrationRepository.findAbsentStudents(attendanceStudentNums);

        }else {
            students = registrationRepository.findAttendStudents(attendanceStudentNums);
        }

        for (Registration student : students) {
            student.setAttendance(attendOrAbsent);
        }

        registrationRepository.saveAll(students);

        return students;
    }

    public void findAttendStudents(List<String> attendanceStudentNums){

    }

    public Optional<Registration> findStudent(String id){
        return registrationRepository.findByStudentNum(id);
    }

}
