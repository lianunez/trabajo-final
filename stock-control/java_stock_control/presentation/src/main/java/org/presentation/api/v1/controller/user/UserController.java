package org.presentation.api.v1.controller.user;

import org.application.permission.PermissionService;
import org.application.role.RoleService;
import org.application.user.UserService;
import org.domain.permission.Permission;
import org.domain.role.Role;
import org.domain.user.User;
import org.domain.user.UserCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;
    private final PermissionService permissionService;

    @Autowired
    public UserController(UserService userService, RoleService roleService, PermissionService permissionService) {
        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<String> create(@RequestBody UserCreateDTO userCreateDTO) {
        User mappedUser = userService.mapUser(userCreateDTO);
        User createdUser = userService.createUser(mappedUser);
        userCreateDTO.getRole().setUserId(createdUser.getId());
        Role role = roleService.create(userCreateDTO.getRole());
        permissionService.createPermission(role, userCreateDTO.getRole().getPermissions());
        return ResponseEntity.ok().body("CREATED!");
    }
}
