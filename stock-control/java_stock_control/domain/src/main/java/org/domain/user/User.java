package org.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.domain.product.Product;
import org.domain.role.Role;

import java.util.List;

@Table(name = "users")
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @JsonProperty("user")
    @Column(name = "name", nullable = false)
    private String userName;

    @JsonProperty("password")
    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(targetEntity = Product.class, cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private List<Product> products;
    @OneToOne(targetEntity = Role.class, cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "user")
    private Role role;
}
