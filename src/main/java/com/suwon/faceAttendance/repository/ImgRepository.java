package com.suwon.faceAttendance.repository;

import com.suwon.faceAttendance.domain.StudentFaceImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImgRepository extends JpaRepository<StudentFaceImg,Long> {

}
