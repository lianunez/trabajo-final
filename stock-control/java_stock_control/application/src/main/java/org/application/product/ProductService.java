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
import java.util.NoSuchElementException;
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
        transfers.stream()
                .forEach(transfer -> {
                    String stringifyTransferredProducts = stringifyTransferredProducts(transfer.getTransfers());
                    transfer.setTransferredProducts(stringifyTransferredProducts);
                    transfer.setTransfers(null);
                });
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
        transfers.set(transferDTO.getProducts().stream()
                .map(dto -> {
                            Integer productStock = productStock(dto.getProductId(), products);
                            if (productStock >= dto.getAmount()) {
                                return ProductHasTransfer.builder()
                                        .amount(dto.getAmount())
                                        .product(Product.builder().id(dto.getProductId()).build())
                                        .transfer(transfer)
                                        .build();
                            }
                            return null;
                        }
                )
                .filter(dto -> dto != null)
                .collect(Collectors.toList()));

        return transfers.get();
    }

    private Integer productStock(Integer id, List<Product> products) {
        Product product = products.stream()
                .filter(prod -> prod.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Transfer not found with ID: " + id));
        return product.getAmount();
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

    private String stringifyTransferredProducts(List<ProductHasTransfer> transferredProducts) {
        StringBuilder stringifyTransferredProducts = new StringBuilder();
        List<Integer> ids = transferredProducts.stream()
                .map(transfer -> transfer.getProduct().getId())
                .collect(Collectors.toList());
        List<Product> products = repository.getProductsById(ids);
        products.forEach(product -> {
            transferredProducts.stream()
                    .filter(t -> t.getProduct().getId().equals(product.getId()))
                    .findFirst()
                    .ifPresent(transfer -> {
                        String stringifyTransfer = product.getName() + ": " + transfer.getAmount() + "<br>";
                        stringifyTransferredProducts.append(stringifyTransfer);
                    });
        });

        return stringifyTransferredProducts.toString();
    }
}
