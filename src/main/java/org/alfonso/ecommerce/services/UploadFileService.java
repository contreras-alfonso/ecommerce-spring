package org.alfonso.ecommerce.services;

import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {
    String uploadImage(MultipartFile file, String folderPath);
}
