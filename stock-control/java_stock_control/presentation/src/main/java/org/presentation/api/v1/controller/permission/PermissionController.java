package org.presentation.api.v1.controller.permission;

import org.application.permission.PermissionService;
import org.domain.permission.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/permissions")
public class PermissionController {
    private final PermissionService service;

    @Autowired
    public PermissionController(PermissionService service) {
        this.service = service;
    }

    @GetMapping(name = "/check")
    public ResponseEntity<List<Permission>> getPermissions() {
        return ResponseEntity.ok().body(service.getPermissions());
    }


}
