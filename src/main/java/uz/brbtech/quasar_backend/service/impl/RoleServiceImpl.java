package uz.brbtech.quasar_backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import uz.brbtech.quasar_backend.dto.request.RoleRequest;
import uz.brbtech.quasar_backend.dto.response.Response;
import uz.brbtech.quasar_backend.dto.response.RoleResponse;
import uz.brbtech.quasar_backend.dto.search.RoleSearchRequest;
import uz.brbtech.quasar_backend.entity.RoleEntity;
import uz.brbtech.quasar_backend.enums.Status;
import uz.brbtech.quasar_backend.exception.CustomException;
import uz.brbtech.quasar_backend.repository.RoleRepository;
import uz.brbtech.quasar_backend.service.RoleService;
import uz.brbtech.quasar_backend.specification.RoleSpecification;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Response<?> createRole(RoleRequest request) {
        RoleEntity entity = RoleEntity.builder()
                .name(request.getName())
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        roleRepository.save(entity);
        return Response.success("Role created successfully");
    }

    @Override
    public Response<?> getRole(Long id) {
        RoleEntity role = roleRepository.findById(id)
                .orElseThrow(() -> CustomException.notFound("Role not found"));
        RoleResponse roleResponse = new RoleResponse(
                role.getId(),
                role.getName(),
                role.getStatus(),
                role.getCreatedAt(),
                role.getUpdatedAt()
        );
        return Response.success("Role successfully found", roleResponse);
    }

    @Override
    public Response<?> getAllRole(Pageable pageable, RoleSearchRequest request) {
        Specification<RoleEntity> specification = RoleSpecification.search(request);

        Page<RoleEntity> page = roleRepository.findAll(specification, pageable);

        List<RoleEntity> roles = page.getContent();

        List<RoleResponse> roleResponses = roles.stream()
                .map(role -> new RoleResponse(
                        role.getId(),
                        role.getName(),
                        role.getStatus(),
                        role.getCreatedAt(),
                        role.getUpdatedAt()
                ))
                .toList();

        return Response.success("All roles successfully found", roleResponses, page.getTotalElements(), page.getTotalPages());
    }

    @Override
    public Response<?> updateRole(RoleRequest request, Long id) {
        RoleEntity entity = roleRepository.findById(id)
                .orElseThrow(() -> CustomException.notFound("Role not found"));
        entity.setName(request.getName());
        entity.setStatus(Status.valueOf(request.getStatus()));
        entity.setUpdatedAt(LocalDateTime.now());
        roleRepository.save(entity);
        return Response.success("Role updated successfully");
    }

    @Override
    public Response<?> deleteRole(Long id) {
        RoleEntity entity = roleRepository.findById(id)
                .orElseThrow(() -> CustomException.notFound("Role not found"));
        entity.setStatus(Status.DELETED);
        roleRepository.save(entity);
        return Response.success("Role deleted successfully");
    }
}