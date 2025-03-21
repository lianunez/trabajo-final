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
public class UserDTO {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("user")
    private String userName;
    @JsonProperty("jwt")
    private String jwt;
    @JsonProperty("role")
    private RoleDTO role;
}
