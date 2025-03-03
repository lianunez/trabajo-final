package org.presentation.api.v1.controller.provider;

import org.application.provider.ProviderService;
import org.domain.provider.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/providers")
public class ProviderController {
    private final ProviderService proverService;

    @Autowired
    public ProviderController(ProviderService proverService) {
        this.proverService = proverService;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<Provider> createProvider(@RequestBody Provider provider) {
        Provider persistedProvider = this.proverService.createProvider(provider);
        return ResponseEntity.status(HttpStatus.CREATED).body(persistedProvider);
    }

    @GetMapping(path = "/check")
    public ResponseEntity<List<Provider>> getProviders(){
        List<Provider> providers = proverService.findAll();
        return ResponseEntity.ok().body(providers);
    }
}
