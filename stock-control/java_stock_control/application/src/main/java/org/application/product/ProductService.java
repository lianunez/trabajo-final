package org.application.product;

import org.data.repository.product.ProductRepository;
import org.domain.product.Product;
import org.domain.product.ProductDTO;
import org.domain.provider.Provider;
import org.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repository;

    @Autowired
    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public String helloWorld() {
        return "Hello world from product service!";
    }

    @Transactional
    public void createProduct(ProductDTO productDTO) {
        Product product = mapper(productDTO);
        this.repository.createProduct(product);
    }

    @Transactional
    public void deleteProduct(String id) {
        this.repository.deleteProduct(id);
    }

    @Transactional
    public void updateProduct(ProductDTO productDTO) {
        Product product = mapper(productDTO);
        this.repository.updateProduct(product);
    }

    public List<Product> getProducts() {
        List<Product> products = repository.getProducts();
        checkCriticalStock(products);
        return products;
    }

    private void checkCriticalStock(List<Product> products) {
        products.stream().forEach(product -> {
            if (product.getAmount() <= 2) {
                product.setCriticalStock(true);
            }
        });
    }

    private Product mapper(ProductDTO productDTO) {
        String now = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return Product.builder()
                .id(productDTO.getId() != null ? productDTO.getId() : null)
                .code(productDTO.getCode())
                .name(productDTO.getName())
                .amount(productDTO.getAmount())
                .date(productDTO.getDate() != null ? productDTO.getDate() : now)
                .expiryDate(productDTO.getExpiryDate())
                .user(User.builder().id(productDTO.getUserId()).build())
                .provider(Provider.builder().id(productDTO.getProviderId()).build())
                .build();
    }
}
