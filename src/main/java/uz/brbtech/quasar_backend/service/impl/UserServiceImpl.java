package uz.brbtech.quasar_backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.brbtech.quasar_backend.config.CustomUserDetailsService;
import uz.brbtech.quasar_backend.dto.request.LoginRequest;
import uz.brbtech.quasar_backend.dto.request.RegisterRequest;
import uz.brbtech.quasar_backend.dto.request.UserCreateRequest;
import uz.brbtech.quasar_backend.dto.response.Response;
import uz.brbtech.quasar_backend.dto.response.UserResponse;
import uz.brbtech.quasar_backend.dto.search.UserSearchRequest;
import uz.brbtech.quasar_backend.entity.RoleEntity;
import uz.brbtech.quasar_backend.entity.UserEntity;
import uz.brbtech.quasar_backend.enums.Status;
import uz.brbtech.quasar_backend.exception.CustomException;
import uz.brbtech.quasar_backend.repository.RoleRepository;
import uz.brbtech.quasar_backend.repository.UserRepository;
import uz.brbtech.quasar_backend.service.UserService;
import uz.brbtech.quasar_backend.specification.UserSpecification;
import uz.brbtech.quasar_backend.util.JWTUtil;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
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

    @Override
    public Response<?> getAllUser(Pageable pageable, UserSearchRequest request) {
        Specification<UserEntity> specification = UserSpecification.search(request);

        Page<UserEntity> page = userRepository.findAll(specification, pageable);

        List<UserEntity> users = page.getContent();

        List<UserResponse> userResponses = users.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getFullName(),
                        user.getPhoneNumber(),
                        user.getEmail(),
                        user.getUsername(),
                        user.getBirthDate(),
                        user.getRoles(),
                        user.getStatus(),
                        user.getCreatedAt(),
                        user.getUpdatedAt()
                ))
                .toList();

        return Response.success(userResponses, page.getTotalElements(), page.getTotalPages());
    }

    @Override
    @Transactional(readOnly = true)
    public Response<?> me(UserEntity user) {
        if (user == null) {
            throw CustomException.badRequest("USER IS NULL");
        }
        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getUsername(),
                user.getBirthDate(),
                user.getRoles(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
        return Response.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message("AuthUser successfully found")
                .success(true)
                .data(userResponse)
                .build();
    }

    @Override
    public Response<?> createUser(UserCreateRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw CustomException.badRequest("Username already exists");
        }

        RoleEntity roleUser = roleRepository.findByName("USER")
                .orElseThrow(() -> CustomException.notFound("Role not found"));

        LocalDateTime now = LocalDateTime.now();
        UserEntity user = UserEntity.builder()
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(hashPassword(request.getPassword()))
                .birthDate(request.getBirthDate())
                .roles(new HashSet<>(Set.of(roleUser)))
                .status(Status.ACTIVE)
                .createdAt(now)
                .updatedAt(now)
                .build();

        userRepository.save(user);
        return Response.success("User created successfully");
    }
}
