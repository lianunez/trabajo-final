package org.presentation.api.v1.controller.role;

import org.application.permission.PermissionService;
import org.application.role.RoleService;
import org.domain.role.Role;
import org.domain.role.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/roles")
public class RoleController {
    private final RoleService roleService;
    private final PermissionService permissionService;

    @Autowired
    public RoleController(RoleService roleService, PermissionService permissionService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @GetMapping(path = "/check")
    public ResponseEntity<RoleDTO> getUserRole(@RequestParam("user_id") Integer userId) {
        RoleDTO role = roleService.getUserRole(userId);
        return ResponseEntity.ok().body(role);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<String> create(@RequestBody RoleDTO roleDTO) {
        Role role = roleService.create(roleDTO);
        permissionService.createPermission(role, roleDTO.getPermissions());
        return ResponseEntity.ok().body("CREATED");
    }

    @PutMapping(path = "/update")
    public ResponseEntity<String> update(@RequestBody RoleDTO roleDTO) {
        roleService.updateRole(roleDTO);
        return ResponseEntity.ok().body("UPDATED!");
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<String> deleteUserRole(@RequestParam("user_id") Integer userId) {
        roleService.deleteRole(userId);
        return ResponseEntity.ok().body("REMOVED!");
    }
}
