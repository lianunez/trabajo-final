package org.data.repository.product;

import org.domain.product.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface ProductRepositoryInterface extends JpaRepository<Product, Integer> {
    @EntityGraph(attributePaths = {"user", "provider"})
    List<Product> findAll();

    @Query(nativeQuery = true, value = "SELECT * FROM products WHERE id=:productId")
    Object[] getProductById(@Param("productId") int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE products SET amount=:#{#product.amount}")
    void updateProductAmount(@Param("product") Product product);
}
