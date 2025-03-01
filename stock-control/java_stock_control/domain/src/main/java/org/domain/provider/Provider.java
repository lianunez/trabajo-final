package org.domain.provider;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.domain.product.Product;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "providers")
@Entity
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @JsonProperty("cuit")
    @Column(name = "cuit", nullable = false)
    private String cuit;

    @JsonProperty("name")
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(targetEntity = Product.class, cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "provider")
    @JsonIgnore
    private List<Product> products;
}
