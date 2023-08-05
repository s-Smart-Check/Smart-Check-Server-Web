package com.suwon.faceAttendance.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class StudentImgDto {

    private Long imgId;
    private String studentNum;
    private MultipartFile attachFile;
    private List<MultipartFile> imageFiles;
}
