package com.example.upload;


import com.example.upload.base.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AcceptanceTest {
    @Autowired
    public FileService fileService;

    private String fileLocation = "../src/main/resources/video3.mp4";

}
