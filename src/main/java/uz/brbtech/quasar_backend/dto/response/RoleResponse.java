package uz.brbtech.quasar_backend.dto.response;

import uz.brbtech.quasar_backend.enums.Status;

import java.time.LocalDateTime;

public record RoleResponse(
        Long id,
        String name,
        Status status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}