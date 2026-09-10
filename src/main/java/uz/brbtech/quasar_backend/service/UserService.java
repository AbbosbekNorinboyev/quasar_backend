package uz.brbtech.quasar_backend.service;

import org.springframework.data.domain.Pageable;
import uz.brbtech.quasar_backend.dto.request.LoginRequest;
import uz.brbtech.quasar_backend.dto.request.RegisterRequest;
import uz.brbtech.quasar_backend.dto.request.UserCreateRequest;
import uz.brbtech.quasar_backend.dto.response.Response;
import uz.brbtech.quasar_backend.dto.search.UserSearchRequest;
import uz.brbtech.quasar_backend.entity.UserEntity;

public interface UserService {

    Response<?> register(RegisterRequest registerRequest);

    Response<?> login(LoginRequest loginRequest);

    Response<?> getAllUser(Pageable pageable, UserSearchRequest request);

    Response<?> me(UserEntity user);

    Response<?> createUser(UserCreateRequest request);
}
