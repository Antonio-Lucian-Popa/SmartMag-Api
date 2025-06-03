package com.asusoftware.SmartMag_Api.file_upload.service;

import com.asusoftware.SmartMag_Api.file_upload.model.FileUpload;
import com.asusoftware.SmartMag_Api.file_upload.repository.FileUploadRepository;
import com.asusoftware.SmartMag_Api.user.model.User;
import com.asusoftware.SmartMag_Api.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@AllArgsConstructor
@Service
public class FileStorageService {

    private final FileUploadRepository fileUploadRepository;
    private final UserRepository userRepository;

    @Value("${file.upload.base-url:/uploads/images/}")
    private String externalLinkBase;

    public String saveFile(MultipartFile file, UUID keycloakId, String entityType, UUID entityId) {
        if (file.isEmpty()) throw new IllegalArgumentException("Empty file not allowed");

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        try {
            String originalFilename = file.getOriginalFilename();
            String storedFilename = UUID.randomUUID() + "_" + originalFilename;
            Path directory = Paths.get("uploads/images", entityType, entityId.toString()).toAbsolutePath().normalize();
            Files.createDirectories(directory);
            Path destination = directory.resolve(storedFilename);

            if (!destination.getParent().equals(directory)) {
                throw new SecurityException("Invalid path detected");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            }

            String fileUrl = externalLinkBase + entityType + "/" + entityId + "/" + storedFilename;

            FileUpload upload = FileUpload.builder()
                    .userId(user.getId())
                    .fileName(originalFilename)
                    .fileUrl(fileUrl)
                    .entityType(entityType)
                    .entityId(entityId)
                    .build();

            fileUploadRepository.save(upload);
            return fileUrl;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}

