package com.suwon.faceAttendance.service;

import com.suwon.faceAttendance.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void 회원가입(){
        User user = new User();
        user.setUserId(2L);
        user.setPassword("asd");
        user.setName("ghdrlfehd");

        User user2 = new User();
        user2.setUserId(3L);
        user2.setPassword("asd123");
        user2.setName("gfehd123123");

        userService.join(user);
        userService.join(user2);
    }
}
