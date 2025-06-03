package com.asusoftware.SmartMag_Api.file_upload.service;

import com.asusoftware.SmartMag_Api.file_upload.model.FileUpload;
import com.asusoftware.SmartMag_Api.file_upload.model.dto.FileUploadDto;
import com.asusoftware.SmartMag_Api.file_upload.repository.FileUploadRepository;
import com.asusoftware.SmartMag_Api.user.model.User;
import com.asusoftware.SmartMag_Api.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class FileStorageService {

    private final FileUploadRepository fileUploadRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Value("${file.upload.base-url:/uploads/images/}")
    private String externalLinkBase;

    public FileUploadDto saveFile(MultipartFile file, UUID keycloakId, String entityType, UUID entityId) {
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

            return modelMapper.map(fileUploadRepository.save(upload), FileUploadDto.class);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public List<FileUploadDto> getFilesForEntity(String entityType, UUID entityId) {
        return fileUploadRepository.findAllByEntityTypeAndEntityId(entityType, entityId)
                .stream()
                .map(upload -> FileUploadDto.builder()
                        .id(upload.getId())
                        .userId(upload.getUserId())
                        .fileName(upload.getFileName())
                        .fileUrl(upload.getFileUrl())
                        .entityType(upload.getEntityType())
                        .entityId(upload.getEntityId())
                        .uploadedAt(upload.getUploadedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFilesForEntity(String entityType, UUID entityId) {
        List<FileUpload> uploads = fileUploadRepository.findAllByEntityTypeAndEntityId(entityType, entityId);

        for (FileUpload upload : uploads) {
            try {
                Path path = Paths.get("uploads/images", entityType, entityId.toString(), upload.getFileName()).toAbsolutePath();
                Files.deleteIfExists(path);
            } catch (IOException e) {
                // Poți loga sau ignora – nu vrem să blocăm ștergerea din DB dacă fișierul nu mai există
            }
        }

        fileUploadRepository.deleteAll(uploads);
    }


}

