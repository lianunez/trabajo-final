package org.domain.transfer;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToMany(targetEntity = ProductHasTransfer.class, cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "transfer")
    @JsonIgnore
    private List<ProductHasTransfer> transfers;
}
