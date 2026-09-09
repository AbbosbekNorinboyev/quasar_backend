package uz.brbtech.quasar_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.brbtech.quasar_backend.dto.request.LoginRequest;
import uz.brbtech.quasar_backend.dto.request.RegisterRequest;
import uz.brbtech.quasar_backend.dto.response.Response;
import uz.brbtech.quasar_backend.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public Response<?> register(@RequestBody RegisterRequest registerRequest) {
        return userService.register(registerRequest);
    }

    @PostMapping("/login")
    public Response<?> login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }
}