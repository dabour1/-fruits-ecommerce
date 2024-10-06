package com.springBoot.fruits_ecommerce.services;

import org.springframework.stereotype.Service;

import org.springframework.core.io.Resource;

import org.springframework.core.io.UrlResource;

import org.springframework.web.multipart.MultipartFile;

import com.springBoot.fruits_ecommerce.exception.FileStorageException;

import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Path;

import java.nio.file.StandardCopyOption;

import java.util.UUID;

import java.nio.file.Paths;

@Service
public class ImageService {

    private final String uploadDir = "files/";

    public String saveImage(MultipartFile image) {

        if (image.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty");
        }
        try {
            String imageName = UUID.randomUUID().toString() + "_" +
                    image.getOriginalFilename();
            if (imageName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + imageName);
            }
            Path imagePath = Paths.get(uploadDir, imageName);
            Files.createDirectories(imagePath.getParent());
            Files.copy(image.getInputStream(), imagePath,
                    StandardCopyOption.REPLACE_EXISTING);

            return imageName;
        } catch (IOException e) {
            throw new RuntimeException("Error saving file: " +
                    image.getOriginalFilename(), e);
        }
    }

    public Resource loadImage(String imageName) {
        Path imagePath = Paths.get(uploadDir).resolve(imageName).normalize();
        try {
            Resource resource = new UrlResource(imagePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + imageName);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading file: " + imageName, e);
        }

    }
}
