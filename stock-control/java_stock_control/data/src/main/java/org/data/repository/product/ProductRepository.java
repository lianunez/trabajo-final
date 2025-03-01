package org.data.repository.product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.domain.product.Product;
import org.domain.product.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class ProductRepository {
    private final ProductRepositoryInterface repoInterface;
    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public ProductRepository(ProductRepositoryInterface repoInterface, EntityManager entityManager) {
        this.repoInterface = repoInterface;
        this.entityManager = entityManager;
    }

    @Transactional
    public void createProduct(Product product) {
        try {
            Objects.requireNonNull(product);
            entityManager.persist(product);
        } catch (Exception e) {
            throw new RuntimeException("Error inserting product: " + e.getMessage(), e);
        }
    }

    public List<Product> getProducts() {
        return repoInterface.findAll();
    }

    @Transactional
    public void deleteProduct(String id) {
        Product foundProduct = entityManager.find(Product.class, String.valueOf(id));
        if (foundProduct != null) {
            entityManager.remove(foundProduct);
        }
    }

    @Transactional
    public void updateProduct(Product product){
        try {
            Objects.requireNonNull(product);
            entityManager.merge(product);
        } catch (Exception e) {
            throw new RuntimeException("Error updating product: " + e.getMessage(), e);
        }
    }
}