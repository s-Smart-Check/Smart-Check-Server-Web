package com.suwon.faceAttendance.controller;

import com.suwon.faceAttendance.domain.StudentFaceImg;
import com.suwon.faceAttendance.domain.UploadFile;
import com.suwon.faceAttendance.file.FileStore;
import com.suwon.faceAttendance.repository.ImgRepository;
import com.suwon.faceAttendance.websocket.Greeting;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImgRepository imgRepository;
    private final FileStore fileStore;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${file.dir}")
    private String fileDir;

    @Value("${pithonURL}")
    private String pURL;

    @PostMapping("/image")
    public ResponseEntity<Object> saveImage(@RequestParam("studentNum") String studentNum,
                            @RequestParam("profile") List<MultipartFile> files)
                            throws IOException {

        //이미지 로컬저장소에 저장
        List<UploadFile> uploadFiles = fileStore.storeFiles(files,studentNum);
        List<String> images = new ArrayList<>();

        for (UploadFile uploadFile : uploadFiles) {

            File imageFile = fileStore.getFile(uploadFile.getStoreFileName());
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            images.add(base64Image);
        }

        //학번과 이미지를 Json형식으로 담기
        Map<String, Object> body = new HashMap<>();
        body.put("studentNum", studentNum);
        body.put("images", images);

        //저장된 이미지를 라즈베리파이로 이미지 전송
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String url = pURL + "images";

        System.out.println(url);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(url, requestEntity, String.class);

        return ResponseEntity.ok("이미지 전송완료");
    }
}
