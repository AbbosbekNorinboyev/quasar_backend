package uz.brbtech.quasar_backend.service;

import org.springframework.data.domain.Pageable;
import uz.brbtech.quasar_backend.dto.request.RoleRequest;
import uz.brbtech.quasar_backend.dto.response.Response;
import uz.brbtech.quasar_backend.dto.search.RoleSearchRequest;

public interface RoleService {

    Response<?> createRole(RoleRequest request);

    Response<?> getRole(Long id);

    Response<?> getAllRole(Pageable pageable, RoleSearchRequest request);

    Response<?> updateRole(RoleRequest request, Long id);

    Response<?> deleteRole(Long id);
}