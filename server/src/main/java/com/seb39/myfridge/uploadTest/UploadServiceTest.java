package com.seb39.myfridge.uploadTest;

import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.InputStream;

public interface UploadServiceTest {
    void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String fileName);

    String getFileUrl(String fileName);

    void deleteFile(String fileName);

    boolean isFileExist(String fileName);

}
