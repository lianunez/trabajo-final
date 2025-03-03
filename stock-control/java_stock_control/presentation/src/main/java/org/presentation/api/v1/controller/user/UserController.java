package org.presentation.api.v1.controller.user;

import org.application.permission.PermissionService;
import org.application.role.RoleService;
import org.application.user.UserService;
import org.domain.role.Role;
import org.domain.user.User;
import org.domain.user.UserCreateDTO;
import org.domain.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(path = "/check")
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> users = userService.getUsers();
        return ResponseEntity.ok().body(users);
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

    @DeleteMapping(path = "/delete")
    public ResponseEntity<String> delete(@RequestParam("id") Integer id) {
        userService.delete(id);
        return ResponseEntity.ok().body("REMOVED!");
    }
}
