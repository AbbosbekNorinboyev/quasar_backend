package uz.brbtech.quasar_backend.dto.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import uz.brbtech.quasar_backend.enums.Status;

@Data
public class UserSearchRequest {

    @Schema(nullable = true)
    private Long id;

    @Schema(defaultValue = "")
    private String fullName;

    @Schema(defaultValue = "")
    private String phoneNumber;

    @Schema(defaultValue = "")
    private String email;

    @Schema(defaultValue = "")
    private String username;

    @Schema(defaultValue = "")
    private String birthDate;

    @Schema(nullable = true)
    private Long roleId;

    private Status status;

    @Schema(defaultValue = "")
    private String createdAt;

    @Schema(defaultValue = "")
    private String updatedAt;
}