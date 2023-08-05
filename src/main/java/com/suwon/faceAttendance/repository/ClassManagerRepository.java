package com.suwon.faceAttendance.repository;

import com.suwon.faceAttendance.domain.ClassManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassManagerRepository extends JpaRepository<ClassManager, Long> {
    ClassManager save(ClassManager classManager);

    List<ClassManager> findAll();

    Optional<ClassManager> findById(Long id);

    Optional<ClassManager> findByClassName(String className);
}
