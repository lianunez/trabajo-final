package org.presentation.api.v1.controller.product;

import org.application.product.ProductService;
import org.domain.product.Product;
import org.domain.product.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<String> createProduct(@RequestBody ProductDTO productDTO) {
        this.service.createProduct(productDTO);
        return ResponseEntity.ok("CREATED!");
    }

    @GetMapping(path = "/check")
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok().body(service.getProducts());
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        service.deleteProduct(id);
        return ResponseEntity.accepted().body("DELETED!");
    }

    @PutMapping(path = "/update")
    public ResponseEntity<String> updateProduct(@RequestBody ProductDTO product) {
        this.service.updateProduct(product);
        return ResponseEntity.ok("UPDATED!");
    }
}
