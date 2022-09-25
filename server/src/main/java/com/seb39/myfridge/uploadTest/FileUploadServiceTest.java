package com.seb39.myfridge.uploadTest;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.seb39.myfridge.image.entity.Image;
import com.seb39.myfridge.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileUploadServiceTest {

    private final UploadServiceTest s3Service;
    private final ImageRepository imageRepository;

    public String uploadImage(List<MultipartFile> files) {
        String returnString = "";
        if (files.size() == 0) {
            return "보낸 파일 없음";
        }

        for (MultipartFile file : files) {
            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

//        if (s3Service.isFileExist(fileName)) {
//            s3Service.deleteFile(fileName);
//        }

            try (InputStream inputStream = file.getInputStream()) {
                s3Service.uploadFile(inputStream, objectMetadata, fileName);
            } catch (IOException e) {
                throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다.(%s)", file.getOriginalFilename()));
            }
            /**
             * image 저장 테스트
             * update 할 때 넘겨받은 파일의 size를 통해서 해당 파일이 update 할 파일인지 구분하는듯?
             * 여기에 더해서 isUpdate 라는 컬럼을 만들고, isUpdate라면 삭제하고 다시 넣는 구조인듯?
             */
            int size = imageRepository.findAll().size();

            Image image = new Image();
            image.setIsDeleted("N");
            image.setIdx(size + 1);
            image.setSize(file.getSize());
            image.setOriginalName(file.getOriginalFilename());
            image.setSaveName(fileName);
            imageRepository.save(image);

            String fileUrl = s3Service.getFileUrl(fileName);
            returnString += fileUrl + "\n";
        }


        return returnString;
    }


    public void deleteImage(String fileName) {
        s3Service.deleteFile(fileName);
    }

    //기존 확장자명을 유지하고, 유니크한 파일의 이름을 생성
    //s3에 같은 이름의 파일이 들어가지 않도록
    //s3 images 디렉토리에 저장
    private String createFileName(String originalFileName) {
        return "test" + "/" + UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    //파일의 확장자명을 가져옴
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.format("잘못된 형식의 파일 (%s) 입니다.", fileName));
        }
    }

}
