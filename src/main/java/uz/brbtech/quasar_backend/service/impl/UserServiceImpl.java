package uz.brbtech.quasar_backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import uz.brbtech.quasar_backend.config.CustomUserDetailsService;
import uz.brbtech.quasar_backend.dto.request.LoginRequest;
import uz.brbtech.quasar_backend.dto.request.RegisterRequest;
import uz.brbtech.quasar_backend.dto.response.Response;
import uz.brbtech.quasar_backend.entity.RoleEntity;
import uz.brbtech.quasar_backend.entity.UserEntity;
import uz.brbtech.quasar_backend.enums.Status;
import uz.brbtech.quasar_backend.exception.CustomException;
import uz.brbtech.quasar_backend.repository.RoleRepository;
import uz.brbtech.quasar_backend.repository.UserRepository;
import uz.brbtech.quasar_backend.service.UserService;
import uz.brbtech.quasar_backend.util.JWTUtil;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static uz.brbtech.quasar_backend.util.PasswordHasher.hashPassword;
import static uz.brbtech.quasar_backend.util.PasswordValidator.validatePassword;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public Response<?> register(RegisterRequest request) {
        RoleEntity roleUser = roleRepository.findByName("USER")
                .orElseThrow(() -> CustomException.notFound("Role not found"));
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(roleUser);

        Optional<UserEntity> byUsername = userRepository.findByUsername(request.getUsername());
        if (byUsername.isPresent()) {
            throw CustomException.badRequest("Username already exists");
        }

        UserEntity user = new UserEntity();
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(hashPassword(request.getPassword()));
        user.setBirthDate(request.getBirthDate());
        user.setRoles(roles);
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        return Response.success("User successfully register");
    }


    @Override
    public Response<?> login(LoginRequest loginRequest) {
        UserEntity authUser = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> CustomException.notFound("AuthUser not found by username: " + loginRequest.getUsername()));
        if (!validatePassword(loginRequest.getPassword(), authUser.getPassword())) {
            throw CustomException.badRequest("Invalid password");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getUsername());
        String jwtToken = jwtUtil.generateToken(userDetails.getUsername());
        return Response.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .success(true)
                .message(jwtToken)
                .build();
    }
}
