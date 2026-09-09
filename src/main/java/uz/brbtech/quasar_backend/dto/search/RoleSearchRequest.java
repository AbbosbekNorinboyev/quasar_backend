package uz.brbtech.quasar_backend.dto.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import uz.brbtech.quasar_backend.enums.Status;

@Data
public class RoleSearchRequest {

    @Schema(nullable = true)
    private Long id;

    @Schema(defaultValue = "")
    private String name;

    private Status status;

    @Schema(defaultValue = "")
    private String createdAt;

    @Schema(defaultValue = "")
    private String updatedAt;
}