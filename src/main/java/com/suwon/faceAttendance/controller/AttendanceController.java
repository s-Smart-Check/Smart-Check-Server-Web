package com.suwon.faceAttendance.controller;

import com.suwon.faceAttendance.domain.Attendance;
import com.suwon.faceAttendance.domain.ClassManager;
import com.suwon.faceAttendance.domain.Registration;
import com.suwon.faceAttendance.dto.AttendDto;
import com.suwon.faceAttendance.dto.StdNumDto;
import com.suwon.faceAttendance.dto.StdNumsDto;
import com.suwon.faceAttendance.service.AttendanceService;
import com.suwon.faceAttendance.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AttendanceController {

    private final RegistrationService registrationService;
    private final AttendanceService attendanceService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public AttendanceController(RegistrationService registrationService, AttendanceService attendanceService, SimpMessagingTemplate messagingTemplate) {
        this.registrationService = registrationService;
        this.attendanceService = attendanceService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/test")
    public String home(Model model){


        return "test";
    }

    @PostMapping("/attendance")
    public @ResponseBody ResponseEntity<Object> attendStudents(@RequestBody StdNumsDto stdNumsDto){
        List<String> attendanceStudentNums = List.of(stdNumsDto.getStdId().split(","));
        List<Registration> atdStudents = registrationService.setAttend(attendanceStudentNums, "출석");
        List<Registration> absStudents = registrationService.setAttend(attendanceStudentNums, "결석");
        atdStudents.addAll(absStudents);
        messagingTemplate.convertAndSend("/topic/attendance", atdStudents);

        return ResponseEntity.ok("출석 완료");
    }

    @PostMapping("/checkAttend")
    public @ResponseBody ResponseEntity<Object> checkAttendance(@RequestBody StdNumDto stdNumDto){
        Registration student = registrationService.findStudent(stdNumDto.getUsrNum()).get();

        Map<String, Object> body = new HashMap<>();
        boolean isAttendance;

        if(student.getAttendance().equals("결석")){
            isAttendance = false;
        }else {
            isAttendance = true;
        }

        body.put("attendance", isAttendance);
        body.put("professor", student.getProfessorName());
        body.put("className", student.getClassName());
        body.put("state" , student.getState());

        return ResponseEntity.ok(body);
    }
    @PostMapping("/checkPastAttend")
    public @ResponseBody ResponseEntity<Object> checkPastAttendance(@RequestBody AttendDto attendDto){
        Attendance student = attendanceService.getStudentAttendance(attendDto.getAttendanceDate(), attendDto.getStdId());

        Map<String, Object> body = new HashMap<>();
        boolean isAttendance;

        if(student.getAttendance().equals("결석")){
            isAttendance = false;
        }else {
            isAttendance = true;
        }

        body.put("attendance", isAttendance);
        body.put("professor", student.getProfessorName());
        body.put("className", student.getClassName());

        return ResponseEntity.ok(body);
    }

    @PostMapping("/attendances")
    public @ResponseBody ResponseEntity<Object> attendStudents(@RequestBody ClassManager classManager){
        attendanceService.saveAttendance(classManager.getClassId());
        registrationService.resetAttendance(classManager.getClassId());

        return ResponseEntity.ok("출석반영");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex){
        // 예외 상태 코드와 메시지를 설정하여 ResponseEntity 반환
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/update-attendance")
    public @ResponseBody ResponseEntity<Object> changeTempAttendance(@RequestBody AttendDto attendDto){
        System.out.println(attendDto.getStdId());
        Registration student = registrationService.findStudent(attendDto.getStdId()).get();
        student.setAttendance(attendDto.getAttendance());
        student.setState(1);
        registrationService.modifyAttendance(student, 1L);

        return ResponseEntity.ok("출석 완료");
    }

    @PostMapping("/manual-attendance")
    public @ResponseBody ResponseEntity<Object> changeAttendance(@RequestBody AttendDto attendDto){
        System.out.println("attendDto = " + attendDto.getAttendance());
        System.out.println("attendDto = " + attendDto.getStdId());
        System.out.println("attendDto = " + attendDto.getAttendanceDate());
        Attendance studentAttendance = attendanceService.getStudentAttendance(attendDto.getAttendanceDate(), attendDto.getStdId());
        studentAttendance.setAttendance(attendDto.getAttendance());
        attendanceService.changeAttendStatus(studentAttendance);

        return ResponseEntity.ok("출결 변경 완료");
    }

//    @PostMapping("/absence")
//    public @ResponseBody ResponseEntity<Object> absentStudent(@RequestBody AttendDto attendDto){
//        Registration student = registrationService.findStudent(attendDto.getStdId()).get();
//        student.setAttendance("결석");
//        registrationService.modifyAttendance(student, 1L);
//
//        return ResponseEntity.ok("결석 처리 완료");
//    }

//    @PostMapping("/attendance")
//    public @ResponseBody ResponseEntity<Object> attendTest(@RequestParam("stdId") String stdId){
//        Student student = registrationService.findStudent(stdId).get();
//        student.setAttendance("출석");
//        registrationService.modifyAttendance(student, 1L);
//
//        messagingTemplate.convertAndSend("/topic/attendance", student);
//
//        return ResponseEntity.ok("출석 완료");
//    }
}
