package com.suwon.faceAttendance.controller;

import com.suwon.faceAttendance.domain.Registration;
import com.suwon.faceAttendance.domain.User;
import com.suwon.faceAttendance.dto.UserJoinDto;
import com.suwon.faceAttendance.dto.UserLoginDto;
import com.suwon.faceAttendance.service.RegistrationService;
import com.suwon.faceAttendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

// REST 컨트롤러로 UserController 클래스 정의
@RestController
public class UserController {

    // UserService 인스턴스 변수 선언하고 Spring의 @Autowired 주석을 사용하여 주입
    private final UserService userService;
    private final RegistrationService registrationService;

    @Autowired
    public UserController(UserService userService, RegistrationService registrationService) {
        this.userService = userService;
        this.registrationService = registrationService;
    }

    // 회원가입을 위한 POST 엔드포인트 정의
    @PostMapping("/tempJoin")
    public ResponseEntity<Object> signUp(@RequestBody UserJoinDto userJoinDto){
        // 안드로이드에서 보낸 json형태의 값을 매개변수인 dto객체에 받아서 User객체에 알맞은 데이터를 넣어준다.
        User user = new User();
        user.setUserId(Long.parseLong(userJoinDto.getUsrNum()));
        user.setPassword(userJoinDto.getUsrPasswd());
        user.setName(userJoinDto.getUsrName());
        userService.join(user);

        //수정할부분
        Registration registration = new Registration();
        registration.setStudentNum(userJoinDto.getUsrNum());
        registration.setStudentName(userJoinDto.getUsrName());
        registration.setAttendance("결석");
        registration.setClassId(1L);
        registration.setClassName("테스트");
        registrationService.save(registration);

        // 안드로이드로 보낼 응답 본문에 포함될 메시지와 상태 코드를 설정하여 ResponseEntity 반환
        Map<String, Object> body = new HashMap<>();
        body.put("message", "회원가입 성공");
        body.put("status", HttpStatus.OK);

        return ResponseEntity.ok(body);
    }

    // 사용자 로그인을 위한 POST 엔드포인트 정의
    @PostMapping("/tempLogin")
    public ResponseEntity<Object> login(@RequestBody UserLoginDto userLoginRequest){
        // 안드로이드에서 보낸 json형태의 값을 매개변수인 dto객체에 받아서 변수 id,와 password에 값을 넣어준다.
        Long id = Long.parseLong(userLoginRequest.getUsrNum());
        String password = userLoginRequest.getUsrPasswd();
        User user = userService.login(id, password);

        // 안드로이드로 보낼 응답 본문에 포함될 사용자 아이디와 이름을 설정하여 ResponseEntity 반환
        Map<String, Object> body = new HashMap<>();
        body.put("usrNum", user.getUserId()+"");
        body.put("usrName",user.getName());

        return ResponseEntity.ok(body);
    }

    // DuplicateKeyException 예외 처리 핸들러
    // 중복된 아이디로 회원가입할때 예외 처리
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Object> handleDuplicateKeyException(DuplicateKeyException ex) {
        // 예외 상태 코드와 메시지를 설정하여 ResponseEntity 반환
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", "이미 존재하는 아이디입니다.");

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // IllegalArgumentException 예외 처리 핸들러
    // 존재하지 않는 아이디 또는 비밀번호가 맞지 않은 경우 예외 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex){
        // 예외 상태 코드와 메시지를 설정하여 ResponseEntity 반환
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
