package uz.brbtech.quasar_backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = "fullName can not be null or empty")
    private String fullName;
    @NotBlank(message = "phoneNumber can not be null or empty")
    private String phoneNumber;
    @NotBlank(message = "email can not be null or empty")
    @Email(message = "Email noto'g'ri formatda")
    private String email;
    @NotBlank(message = "username can not be null or empty")
    private String username;
    @NotBlank(message = "password can not be null or empty")
    private String password;
    @NotBlank(message = "birthDate can not be null or empty")
    private String birthDate;
}