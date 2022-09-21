package com.seb39.myfridge.upload;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@RequiredArgsConstructor
@Component
public class AWSS3UploadService implements UploadService{

    private final AmazonS3 amazonS3;
    private final S3Component component;

    @Override
    public void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String fileName) {

        amazonS3.putObject(new PutObjectRequest(component.getBucket(), fileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
    }

    @Override
    public String getFileUrl(String fileName) {
        return amazonS3.getUrl(component.getBucket(), fileName).toString();
    }

    @Override
    public void deleteFile(String fileName) {
        String s3FileName = getFileName(fileName);
        amazonS3.deleteObject(component.getBucket(), s3FileName);
    }

    @Override
    public boolean isFileExist(String fileName) {
        return amazonS3.doesObjectExist(component.getBucket(), fileName);
    }

    private String getFileName(String fileName) {
        return "images/" + fileName.substring(fileName.lastIndexOf("/") + 1);
    }

}
