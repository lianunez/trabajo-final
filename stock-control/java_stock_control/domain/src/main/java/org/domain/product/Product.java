package org.domain.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.domain.productHasTransfer.ProductHasTransfer;
import org.domain.provider.Provider;
import org.domain.transfer.Transfer;
import org.domain.user.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "products")
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonProperty("user_id")
    User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    @JsonProperty("provider_id")
    Provider provider;
    @Transient
    boolean criticalStock;
    @Transient
    boolean warningStock;
    @OneToMany(targetEntity = ProductHasTransfer.class, cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "product")
    @JsonIgnore
    private List<ProductHasTransfer> transfers;
}
