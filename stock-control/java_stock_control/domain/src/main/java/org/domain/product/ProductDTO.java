package org.domain.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.domain.provider.Provider;
import org.domain.user.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("code")
    private String code;
    @JsonProperty("name")
    private String name;
    @JsonProperty("amount")
    private int amount;
    @JsonProperty("expiry_date")
    private String expiryDate;
    @JsonProperty("date")
    private String date;
    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("provider_id")
    private int providerId;
    private User user;
    private Provider provider;
}
