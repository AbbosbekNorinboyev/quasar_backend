package uz.brbtech.quasar_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import uz.brbtech.quasar_backend.dto.request.RoleRequest;
import uz.brbtech.quasar_backend.dto.response.Response;
import uz.brbtech.quasar_backend.dto.search.RoleSearchRequest;
import uz.brbtech.quasar_backend.service.RoleService;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping(value = "/create")
    public Response<?> createRole(@RequestBody RoleRequest request) {
        return roleService.createRole(request);
    }

    @GetMapping(value = "/{roleId}")
    public Response<?> getRole(@PathVariable Long roleId) {
        return roleService.getRole(roleId);
    }

    @GetMapping(value = "/getAll")
    public Response<?> getAllRole(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                  @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                  @ModelAttribute RoleSearchRequest request) {
        return roleService.getAllRole(
                PageRequest.of(page, size, Sort.Direction.ASC, "id"),
                request
        );
    }

    @PutMapping("/update/{roleId}")
    public Response<?> updateRole(@RequestBody RoleRequest request, @PathVariable Long roleId) {
        return roleService.updateRole(request, roleId);
    }

    @DeleteMapping("/delete/{roleId}")
    public Response<?> deleteRole(@PathVariable Long roleId) {
        return roleService.deleteRole(roleId);
    }
}