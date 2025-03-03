package org.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ActionDTO {
    @JsonProperty("triggered_by")
    private Integer trigger;
    @JsonProperty("action")
    private String action;
}
