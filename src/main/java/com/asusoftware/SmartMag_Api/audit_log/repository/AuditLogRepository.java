package com.asusoftware.SmartMag_Api.audit_log.repository;

import com.asusoftware.SmartMag_Api.audit_log.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {}

