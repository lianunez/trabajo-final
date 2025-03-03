package org.domain.role;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RoleDTO {
    @JsonProperty("name")
    private String name;
    @JsonProperty("permissions")
    private List<String> permissions;
    @JsonProperty("user_id")
    private Integer userId;
}
