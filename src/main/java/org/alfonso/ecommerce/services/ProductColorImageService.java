package org.alfonso.ecommerce.services;

import org.springframework.web.multipart.MultipartFile;

public interface ProductColorImageService {
    String upload(MultipartFile file, String folderPath);
}
