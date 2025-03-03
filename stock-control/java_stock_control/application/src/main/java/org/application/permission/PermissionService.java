package org.application.permission;

import jakarta.transaction.Transactional;
import org.data.repository.permission.PermissionRepository;
import org.domain.permission.Permission;
import org.domain.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    private final PermissionRepository repository;

    @Autowired
    public PermissionService(PermissionRepository repository) {
        this.repository = repository;
    }
    @Transactional
    public void createPermission(Role role, List<String> permissions){
        Permission permission = Permission.builder()
                .permissions(permissions)
                .role(role)
                .build();
        repository.createPermission(permission);
    }

    public List<Permission> getPermissions(){
        return repository.getPermissions();
    }
}
