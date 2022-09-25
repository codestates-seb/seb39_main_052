package com.seb39.myfridge.uploadTest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class FileUploadControllerTest {

    private final FileUploadServiceTest fileUploadService;

    //@RequestPart 애너테이션을 사용하여 multipart/form-data 요청을 받을 수 있음
    @PostMapping("/api/upload/test")
    public String uploadImage(@RequestPart List<MultipartFile> files) {
        return fileUploadService.uploadImage(files);
    }

}
