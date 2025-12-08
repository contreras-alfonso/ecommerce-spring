package org.alfonso.ecommerce.services;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface S3Service {
    String uploadFile(MultipartFile file, String folderPath);
}
