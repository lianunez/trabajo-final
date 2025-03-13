package org.domain.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferDTO {
    @JsonProperty("user_id")
    private Integer userId;
    @JsonProperty("area")
    private String area;
    @JsonProperty("products")
    private List<TransferredProductDTO> products;
}
