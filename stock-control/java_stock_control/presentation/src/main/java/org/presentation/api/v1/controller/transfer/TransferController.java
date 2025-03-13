package org.presentation.api.v1.controller.transfer;

import org.application.product.ProductService;
import org.domain.transfer.Transfer;
import org.domain.transfer.TransferDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/transfers")
public class TransferController {
    private final ProductService service;

    @Autowired
    public TransferController(ProductService service) {
        this.service = service;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<String> create(@RequestBody TransferDTO transfers) {
        Objects.requireNonNull(transfers);
        service.transferProducts(transfers);
        return ResponseEntity.ok().body("CREATED!");
    }

    @GetMapping(path = "/check")
    public ResponseEntity<List<Transfer>> getTransfers() {
        List<Transfer> transfers = service.getTransfers();
        return ResponseEntity.ok().body(transfers);
    }
}
