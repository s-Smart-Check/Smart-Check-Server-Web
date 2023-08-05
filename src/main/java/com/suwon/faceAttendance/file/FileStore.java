package com.suwon.faceAttendance.file;

import com.suwon.faceAttendance.domain.StudentFaceImg;
import com.suwon.faceAttendance.domain.UploadFile;
import com.suwon.faceAttendance.repository.ImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    private final ImgRepository imgRepository;
    public String getFullPath(String filename){
        return fileDir + filename;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles,String studentNum) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty()){
                storeFileResult.add(storeFile(multipartFile, studentNum));
            }
        }
        return storeFileResult;
    }

    public UploadFile storeFile(MultipartFile multipartFile,String studentNum) throws IOException {
        if(multipartFile.isEmpty()){
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        //데이터베이스에 저장
        StudentFaceImg img = new StudentFaceImg();
        img.setStudentNum(studentNum);
        img.setUploadFileName(originalFilename);
        img.setStoreFileName(storeFileName);
        imgRepository.save(img);

        return new UploadFile(originalFilename,storeFileName);
    }

    private String createStoreFileName(String originalFilename) {
        //서버에 저장하는 파일명
        String uuid = UUID.randomUUID().toString();

        //확장자명 가져오기
        String ext = extractExt(originalFilename);

        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename){
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    public File getFile(String filename){
        return new File(getFullPath(filename));
    }
}
