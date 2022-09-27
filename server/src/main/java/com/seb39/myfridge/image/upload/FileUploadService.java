package com.seb39.myfridge.image.upload;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.seb39.myfridge.image.entity.Image;
import com.seb39.myfridge.image.repository.ImageRepository;
import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.step.entity.Step;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileUploadService {

    private final UploadService s3Service;
    private final ImageRepository imageRepository;

    //이미지 여러개 업로드
    public void uploadImages(Recipe recipe, List<Step> steps, List<MultipartFile> files) {
        if (!files.isEmpty()) {
            int count = 0;
            for (MultipartFile file : files) {
                String fileName = setFileName(file);
                //레시피 사진 저장
                if (count == 0) {
                    Image image = createImage(0, fileName);
                    recipe.setImage(image);
                    imageRepository.save(image);
                    count++;
                } else {
                    //스탭 사진 저장
                    int idx = steps.get(count - 1).getSequence();
                    Image image = createImage(idx, fileName);
                    steps.get(count - 1).setImage(image);
                    imageRepository.save(image);
                    count++;
                }
            }
        }
    }

    private String setFileName(MultipartFile file) {
        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileName);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다.(%s)", file.getOriginalFilename()));
        }
        return fileName;
    }

    private Image createImage(int idx, String fileName) {
        Image image = new Image();
        image.setIdx(idx);
        image.setIsUpdated("N");
        image.setImagePath(s3Service.getFileUrl(fileName));
        return image;
    }

    public void updateRecipeImages(Recipe recipe, MultipartFile file) {
        String fileName = setFileName(file);
        Image image = createImage(0, fileName);
        recipe.setImage(image);
        imageRepository.save(image);
    }

    public void updateStepImages(Step step, MultipartFile file, int idx) {
        String fileName = setFileName(file);
        Image image = createImage(idx, fileName);
        step.setImage(image);
        imageRepository.save(image);
    }


    public String uploadImage(MultipartFile file) {
        String fileName = setFileName(file);
        return s3Service.getFileUrl(fileName);
    }


    public void deleteImage(String fileName) {
        s3Service.deleteFile(fileName);
    }

    //기존 확장자명을 유지하고, 유니크한 파일의 이름을 생성
    //s3에 같은 이름의 파일이 들어가지 않도록
    //s3 images 디렉토리에 저장
    private String createFileName(String originalFileName) {
        return "images" + "/" + UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
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
