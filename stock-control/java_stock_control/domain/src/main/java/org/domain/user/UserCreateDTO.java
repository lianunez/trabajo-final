package org.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.domain.role.RoleDTO;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCreateDTO {
    @JsonProperty("user_name")
    private String name;
    @JsonProperty("password")
    private String password;
    @JsonProperty("role")
    private RoleDTO role;
}
