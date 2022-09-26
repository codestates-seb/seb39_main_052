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

    public void uploadImages(Recipe recipe, List<Step> steps, List<MultipartFile> files) {
        if (!files.isEmpty()) {
            int count = 0;
            for (MultipartFile file : files) {
                String fileName = createFileName(file.getOriginalFilename());
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(file.getSize());
                objectMetadata.setContentType(file.getContentType());

                try (InputStream inputStream = file.getInputStream()) {
                    s3Service.uploadFile(inputStream, objectMetadata, fileName);
                } catch (IOException e) {
                    throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다.(%s)", file.getOriginalFilename()));
                }

                //레시피 사진 저장
                if (count == 0) {
                    Image image = new Image();
                    image.setSaveName(fileName);
                    image.setOriginalName(file.getOriginalFilename());
                    image.setSize(file.getSize());
                    image.setIdx(0);
                    image.setIsUpdated("N");
                    image.setIsDeleted("N");
                    image.setRecipe(recipe);
                    image.setImagePath(s3Service.getFileUrl(fileName));
                    image.addRecipeImage(recipe);

                    imageRepository.save(image);
                    count++;
                } else {
                    //스탭 사진 저장

                    Image image = new Image();
                    image.setSaveName(fileName);
                    image.setOriginalName(file.getOriginalFilename());
                    image.setSize(file.getSize());
                    image.setIdx(steps.get(count-1).getSequence());
                    image.setIsUpdated("N");
                    image.setIsDeleted("N");
                    image.setStep(steps.get(count-1));
                    image.setImagePath(s3Service.getFileUrl(fileName));
                    image.setRecipe(recipe);
                    image.addStepImage(steps.get(count-1));
                    imageRepository.save(image);
                    count++;

                }
            }
        }
    }

    public void updateImages(Recipe recipe, List<Step> steps, List<MultipartFile> files, int idx) {
        if (!files.isEmpty()) {
            for (MultipartFile file : files) {
                String fileName = createFileName(file.getOriginalFilename());
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(file.getSize());
                objectMetadata.setContentType(file.getContentType());

                try (InputStream inputStream = file.getInputStream()) {
                    s3Service.uploadFile(inputStream, objectMetadata, fileName);
                } catch (IOException e) {
                    throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다.(%s)", file.getOriginalFilename()));
                }

                //레시피 사진 저장
                if (idx == 0) {
                    Image image = new Image();
                    image.setSaveName(fileName);
                    image.setOriginalName(file.getOriginalFilename());
                    image.setSize(file.getSize());
                    image.setIdx(0);
                    image.setIsUpdated("N");
                    image.setIsDeleted("N");
                    image.setRecipe(recipe);
                    image.setImagePath(s3Service.getFileUrl(fileName));
                    image.addRecipeImage(recipe);

                    imageRepository.save(image);
                } else {
                    //스탭 사진 저장

                    Image image = new Image();
                    image.setSaveName(fileName);
                    image.setOriginalName(file.getOriginalFilename());
                    image.setSize(file.getSize());
                    image.setIdx(idx);
                    image.setIsUpdated("N");
                    image.setIsDeleted("N");
                    image.setStep(steps.get(idx-1));
                    image.setImagePath(s3Service.getFileUrl(fileName));
                    image.addStepImage(steps.get(idx-1));
                    imageRepository.save(image);
                }
            }
        }
    }


    public String uploadImage(MultipartFile file) {
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
        return s3Service.getFileUrl(fileName);
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
