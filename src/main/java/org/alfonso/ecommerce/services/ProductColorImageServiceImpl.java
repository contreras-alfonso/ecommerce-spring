package org.alfonso.ecommerce.services;

import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.exceptions.FileTooLargeException;
import org.alfonso.ecommerce.exceptions.InvalidFileFormatException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductColorImageServiceImpl implements ProductColorImageService {

    private final S3Service s3Service;

    @Value("${file.upload.max-size-bytes}")
    private long MAX_FILE_SIZE;

    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    @Override
    public String upload(MultipartFile file, String folderPath) {
        if (file.getSize() > MAX_FILE_SIZE) {
            double maxSizeMb = (double) MAX_FILE_SIZE / (1024 * 1024);
            throw new FileTooLargeException("El tamaño del archivo excede el límite permitido de " + maxSizeMb);
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new InvalidFileFormatException("Formato de archivo no permitido. Solo se aceptan: JPG, PNG, WEBP.");
        }
        return s3Service.uploadFile(file, folderPath);
    }
}
