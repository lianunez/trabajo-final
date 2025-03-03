package org.application.role;

import jakarta.transaction.Transactional;
import org.data.repository.role.RoleRepository;
import org.domain.role.Role;
import org.domain.role.RoleDTO;
import org.domain.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private final RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Role create(RoleDTO roleDTO) {
        Role role = mapper(roleDTO);
        repository.createRole(role);
        return role;
    }

    @Transactional
    public void deleteRole(Integer userId) {
        Role foundRole = repository.getUserRole(userId);
        repository.deleteRole(foundRole.getId());
    }

    public RoleDTO getUserRole(Integer userId) {
        RoleDTO role = reverseMapper(repository.getUserRole(userId));
        return role;
    }

    @Transactional
    public void updateRole(RoleDTO roleDTO) {
        Role foundRole = repository.getUserRole(roleDTO.getUserId());
        foundRole.getPermission().setPermissions(roleDTO.getPermissions());
        repository.updateRole(foundRole);
    }

    public List<RoleDTO> getRoles() {
        List<Role> roles = repository.getRoles();
        List<RoleDTO> mappedRoles = roles.stream()
                .map(role -> {
                    return RoleDTO.builder()
                            .name(role.getName())
                            .permissions(role.getPermission().getPermissions())
                            .userId(role.getUser().getId())
                            .build();
                })
                .collect(Collectors.toList());
        return mappedRoles;
    }

    private Role mapper(RoleDTO roleDTO) {
        return Role.builder()
                .name(roleDTO.getName())
                .user(User.builder().id(roleDTO.getUserId()).build())
                .build();
    }

    private RoleDTO reverseMapper(Role role) {
        return RoleDTO.builder()
                .name(role.getName())
                .permissions(role.getPermission().getPermissions())
                .userId(role.getUser().getId())
                .build();
    }
}
