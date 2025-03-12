package org.domain.transfer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.domain.productHasTransfer.ProductHasTransfer;
import org.domain.user.User;

import java.util.List;

@Entity
@Table( name = "transfers")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "date")
    private String date;
    private String area;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToMany(targetEntity = ProductHasTransfer.class, cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "transfer")
    @JsonManagedReference
    private List<ProductHasTransfer> transfers;
    @Transient
    private String transferredProducts;
}
