package com.suwon.faceAttendance.controller;

import com.suwon.faceAttendance.websocket.Greeting;
import com.suwon.faceAttendance.websocket.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class WebSocketController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) {

        return new Greeting();
    }
}
