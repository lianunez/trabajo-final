package org.security.roles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.application.user.UserService;
import org.data.repository.user.UserRepository;
import org.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoleCheckService {
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    @Autowired
    public RoleCheckService(ObjectMapper objectMapper, UserRepository userRepository) {
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
    }

    public boolean hasAccess(String username, String uri, String requestPayload) throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> parsedPayload = objectMapper.readValue(requestPayload, new TypeReference<Map<String, Object>>() {
            });
            Map<String, String> headers = (Map<String, String>) parsedPayload.get("headers");
            String requestBody = (String) parsedPayload.get("body");
            Integer userId = Integer.parseInt(headers.get("trigger-user"));
            User user = userRepository.findById(userId);

            return hasPermissions(uri, user);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error trying check user permissions", e);
        }
    }

    private boolean hasPermissions(String uri, User user) {
        String resource = extractResource(uri);

        if (resource.length() > 0) {
            boolean hasAccess = user.getRole().getPermission().getPermissions().stream()
                    .anyMatch(permission -> permission.matches(resource));
            return hasAccess;
        }
        return false;
    }

    private String extractResource(String uri) {
        String[] parts = uri.split("/");

        if (parts.length <= 3) {
            return "";
        }

        return Arrays.stream(parts, 3, parts.length) // Stream from index 3 onwards
                .collect(Collectors.joining("."));
    }
}
