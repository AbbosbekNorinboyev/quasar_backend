package uz.brbtech.quasar_backend.service;

import uz.brbtech.quasar_backend.dto.request.LoginRequest;
import uz.brbtech.quasar_backend.dto.request.RegisterRequest;
import uz.brbtech.quasar_backend.dto.response.Response;

public interface UserService {

    Response<?> register(RegisterRequest registerRequest);

    Response<?> login(LoginRequest loginRequest);
}
