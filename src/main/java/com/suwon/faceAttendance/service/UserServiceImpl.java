package com.suwon.faceAttendance.service;

import com.suwon.faceAttendance.domain.User;
import com.suwon.faceAttendance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void join(User user) {
        validateDuplicateUser(user);
        userRepository.save(user);

    }

    public List<User> findUsers() {
        return userRepository.findAll();
    }

    public User login(Long id, String password) throws DuplicateKeyException,IllegalArgumentException{
        User findUser = validateExistUser(id);
        validateConfirmPassword(password,findUser);

        return findUser;
    }

    public void validateDuplicateUser(User user) throws DuplicateKeyException{
        userRepository.findByUserId(user.getUserId())
                .ifPresent(u ->{
                    throw new DuplicateKeyException("이미 존재하는 아이디입니다.");
                });
    }

    public User validateExistUser(Long id) throws IllegalArgumentException{

        User findUser= userRepository.findByUserId(id)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        return findUser;
    }

    public void validateConfirmPassword(String password,User user) throws IllegalArgumentException{
        if(!password.equals(user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
        }
    }
}
