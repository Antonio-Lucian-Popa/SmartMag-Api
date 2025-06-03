package com.asusoftware.SmartMag_Api.file_upload.repository;

import com.asusoftware.SmartMag_Api.file_upload.model.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, UUID> {

    List<FileUpload> findAllByEntityTypeAndEntityId(String entityType, UUID entityId);

    List<FileUpload> findAllByUserId(UUID userId);

    void deleteAllByEntityTypeAndEntityId(String entityType, UUID entityId);
}
