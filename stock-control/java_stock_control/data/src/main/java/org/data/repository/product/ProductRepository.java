package org.data.repository.product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
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

    @Transactional
    public List<Product> getProducts() {
        try {
            String sql = "SELECT p FROM Product p " +
                    "LEFT JOIN FETCH p.user u " +  // Eagerly fetch User
                    "LEFT JOIN FETCH p.provider pr"; // Eagerly fetch Provider
            TypedQuery<Product> query = entityManager.createQuery(sql, Product.class);
            List<Product> products = query.getResultList();
            return products;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching products: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deleteProduct(String id) {
        try {
            Product foundProduct = entityManager.find(Product.class, String.valueOf(id));
            if (foundProduct != null) {
                entityManager.remove(foundProduct);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error removing product: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void updateProduct(Product product) {
        try {
            Objects.requireNonNull(product);
            entityManager.merge(product);
        } catch (Exception e) {
            throw new RuntimeException("Error updating product: " + e.getMessage(), e);
        }
    }

    public Product getProductById(Integer id) {
        try {
            Objects.requireNonNull(id);
            return entityManager.find(Product.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching product: " + e.getMessage(), e);
        }
    }

    public List<Product> getProductsById(List<Integer> ids) {
        try {
            String sql = "SELECT p FROM Product p WHERE p.id IN :ids";
            List<Product> products = entityManager.createQuery(sql, Product.class)
                    .setParameter("ids", ids)
                    .getResultList();
            return products;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching products: " + e.getMessage(), e);
        }
    }
}