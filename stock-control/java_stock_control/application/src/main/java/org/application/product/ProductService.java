package org.application.product;

import jakarta.transaction.Transactional;
import org.data.repository.product.ProductRepository;
import org.data.repository.transfer.TransferRepository;
import org.domain.product.Product;
import org.domain.product.ProductDTO;
import org.domain.productHasTransfer.ProductHasTransfer;
import org.domain.provider.Provider;
import org.domain.transfer.Transfer;
import org.domain.transfer.TransferDTO;
import org.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository repository;
    private final TransferRepository transferRepository;

    @Autowired
    public ProductService(ProductRepository repository, TransferRepository transferRepository) {
        this.repository = repository;
        this.transferRepository = transferRepository;
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

    public List<Transfer> getTransfers() {
        List<Transfer> transfers = transferRepository.getTransfers();
        return transfers;
    }

    @Transactional
    public void transferProducts(TransferDTO transfers) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        List<Integer> ids = transfers.getProducts().stream()
                .map(transfer -> transfer.getProductId())
                .collect(Collectors.toList());
        List<Product> products = repository.getProductsById(ids);
        Transfer transfer = Transfer.builder()
                .date(date)
                .area(transfers.getArea())
                .user(User.builder().id(transfers.getUserId()).build())
                .build();
        transferRepository.createTransfer(transfer);
        List<ProductHasTransfer> productHasTransfers = createProductHasTransfer(transfers, transfer, products);
        transfer.setTransfers(productHasTransfers);
        transferRepository.createProductTransfer(transfer);
        updateProductStock(transfers, products);
    }

    private List<ProductHasTransfer> createProductHasTransfer(TransferDTO transferDTO, Transfer transfer, List<Product> products) {
        AtomicReference<List<ProductHasTransfer>> transfers = new AtomicReference<>(new ArrayList<ProductHasTransfer>());

        products.stream().forEach(product -> {
            transfers.set(transferDTO.getProducts().stream()
                    .map(dto -> {
                        ProductHasTransfer productHasTransfer = null;
                        if (product.getId() == dto.getProductId() && product.getAmount() >= dto.getAmount()) {
                            productHasTransfer = ProductHasTransfer.builder()
                                    .amount(dto.getAmount())
                                    .product(Product.builder().id(dto.getProductId()).build())
                                    .transfer(transfer)
                                    .build();
                        }
                        return productHasTransfer;
                    })
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList()));
        });

        return transfers.get();
    }

    @Transactional
    private void updateProductStock(TransferDTO transfers, List<Product> products) {
        transfers.getProducts().stream().forEach(transferDTO -> {
            products.stream()
                    .forEach(product -> {
                        if (product.getId().equals(transferDTO.getProductId())) {
                            product.setAmount(product.getAmount() - transferDTO.getAmount());
                            repository.updateProduct(product);
                        }
                    });
        });
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
