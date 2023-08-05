package com.suwon.faceAttendance.controller;

import com.suwon.faceAttendance.domain.Attendance;
import com.suwon.faceAttendance.domain.Registration;
import com.suwon.faceAttendance.service.AttendanceService;
import com.suwon.faceAttendance.service.RegistrationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class HomeController {

    private final RegistrationService registrationService;
    private final AttendanceService attendanceService;

    public HomeController(RegistrationService registrationService, AttendanceService attendanceService) {
        this.registrationService = registrationService;
        this.attendanceService = attendanceService;
    }

    @GetMapping("/userTest")
    public String home(){
        return "userTest";
    }

    @GetMapping("/class")
    public String studentList(Model model){

        List<Registration> students = registrationService.findStudents();
        model.addAttribute("list", students);

        return "class";
    }

    @GetMapping("/attendances")
    public String attendanceList(Model model){

        List<Registration> students = registrationService.findStudents();
        model.addAttribute("list", students);

        return "attendances";
    }

    @GetMapping("/attendance/detail")
    public String getAttendanceDetail(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, Model model) {
        //테스트로 수업코드를 1로 지정해둠. 나중에 웹에서 수업코드를 보내서 처리하도록 변경
        List<Attendance> students = attendanceService.getStudentsAttendances(date, 1L);
        model.addAttribute("list", students);
        model.addAttribute("date", date);

        return "attendance/detail";
    }

    @GetMapping("/imgTest")
    public String imgHome(){
        return "imgTest";
    }
}
