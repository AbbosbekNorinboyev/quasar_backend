package uz.brbtech.quasar_backend.dto.response;

import uz.brbtech.quasar_backend.entity.RoleEntity;
import uz.brbtech.quasar_backend.enums.Status;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponse(
        Long id,
        String fullName,
        String phoneNumber,
        String email,
        String username,
        String birthDate,
        Set<RoleEntity> roles,
        Status status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}