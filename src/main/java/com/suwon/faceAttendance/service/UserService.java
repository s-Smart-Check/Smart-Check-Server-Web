package com.suwon.faceAttendance.service;

import com.suwon.faceAttendance.domain.User;

import java.util.List;

public interface UserService {

    void join(User user);

    List<User> findUsers();

    User login(Long id, String password);
}
