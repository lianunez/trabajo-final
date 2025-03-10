package org.domain.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferredProductDTO {
    @JsonProperty("product_id")
    private Integer productId;
    @JsonProperty("amount")
    private Integer amount;
}
