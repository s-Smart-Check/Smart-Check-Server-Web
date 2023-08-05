package com.suwon.faceAttendance.service;

import com.suwon.faceAttendance.domain.ClassManager;
import com.suwon.faceAttendance.repository.ClassManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassManagerService {
    private ClassManagerRepository classManagerRepository;

    @Autowired
    public ClassManagerService(ClassManagerRepository classManagerRepository) {
        this.classManagerRepository = classManagerRepository;
    }

    public ClassManager createClass(ClassManager classManager){
        return classManagerRepository.save(classManager);
    }

    public Optional<ClassManager> findClassInfo(Long classId){

        return classManagerRepository.findById(classId);
    }

    public ClassManager modifyClassStartTime(ClassManager classManager){

        return classManagerRepository.save(classManager);
    }

    public List<ClassManager> findAll(){
        return classManagerRepository.findAll();
    }
}
