package com.springBoot.fruits_ecommerce.services;

import org.springframework.stereotype.Service;

import org.springframework.core.io.Resource;

import org.springframework.core.io.UrlResource;

import org.springframework.web.multipart.MultipartFile;

import com.springBoot.fruits_ecommerce.exception.FileStorageException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Path;

import java.nio.file.StandardCopyOption;

import java.util.UUID;

import java.nio.file.Paths;

@Service
public class ImageService {

    private final String uploadDir = "uploads/files/";

    public String saveImage(MultipartFile image) {
        validateImage(image);

        String fileName = generateUniqueFileName(image);
        validateFileName(fileName);

        ensureUploadDirExists();

        return storeImage(image, fileName);
    }

    private void validateImage(MultipartFile image) {
        if (image.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty");
        }
    }

    private String generateUniqueFileName(MultipartFile image) {
        return uploadDir + UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
    }

    private void validateFileName(String fileName) {
        if (fileName.contains("..")) {
            throw new IllegalArgumentException("Invalid path sequence in file name: " + fileName);
        }
    }

    private void ensureUploadDirExists() {
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private String storeImage(MultipartFile image, String fileName) {
        try {
            Path path = Paths.get(fileName);
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Error saving file: " + image.getOriginalFilename(), e);
        }
    }

}
