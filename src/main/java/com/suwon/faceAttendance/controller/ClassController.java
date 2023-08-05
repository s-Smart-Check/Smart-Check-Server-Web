package com.suwon.faceAttendance.controller;

import com.suwon.faceAttendance.domain.ClassManager;
import com.suwon.faceAttendance.domain.Registration;
import com.suwon.faceAttendance.service.RegistrationService;
import com.suwon.faceAttendance.service.ClassManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ClassController {
    private final ClassManagerService classManagerService;

    private final RegistrationService registrationService;

    @Value("${pithonURL}")
    private String pURL;

    @Autowired
    public ClassController(ClassManagerService classManagerService, RegistrationService registrationService) {
        this.classManagerService = classManagerService;
        this.registrationService = registrationService;
    }

    @MessageMapping("/startClass")
    public void startClass(@RequestBody ClassManager classManager) throws IOException{
        classManager = classManagerService.findClassInfo(classManager.getClassId()).get();
        LocalTime now = LocalTime.now();
        classManager.setStartTime(now);
        classManagerService.modifyClassStartTime(classManager);

        transmissionClassId(classManager,"start");

    }

    @MessageMapping("/stopClass")
    public void stopClass(@RequestBody ClassManager classManager) throws IOException{
        transmissionClassId(classManager,"stop");

    }


    public void transmissionClassId(ClassManager classManager, String control) throws IOException {
        Map<String, Object> body = new HashMap<>();
        body.put("classId", classManager.getClassId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String url = pURL + control;

        RestTemplate restTemplate = new RestTemplate();
        String s = restTemplate.postForObject(url, requestEntity, String.class);
        System.out.println(s);
    }
}
