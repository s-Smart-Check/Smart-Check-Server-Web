package com.suwon.faceAttendance.repository;

import com.suwon.faceAttendance.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class SpringDataJpaUserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test void save(){
        //given
        User user = new User();
        user.setUserId(3L);
        user.setPassword("asd");
        user.setName("asd");

        //when
        userRepository.save(user);

        //then
        User result = userRepository.findByUserId(3L).get();
        assertThat(result.getName()).isEqualTo(user.getName());

    }

    @Test
    void findByUserId(){
        //given
        User user1 = new User();
        user1.setUserId(1L);
        user1.setPassword("asd");
        User user2 = new User();
        user2.setUserId(2L);
        user2.setPassword("asd");
        userRepository.save(user1);
        userRepository.save(user2);

        //when
        User result = userRepository.findByUserId(1L).get();

        //then
        assertThat(result.getUserId()).isEqualTo(user1.getUserId());

    }

}
