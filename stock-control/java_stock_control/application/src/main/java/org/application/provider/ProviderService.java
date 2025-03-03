package org.application.provider;

import org.data.repository.provider.ProviderRepository;
import org.domain.provider.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class ProviderService {
    private final ProviderRepository providerRepository;

    @Autowired
    public ProviderService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    @Transactional
    public Provider createProvider(Provider provider) {
        return this.providerRepository.createProvider(provider);
    }

    public List<Provider> findAll() {
        return providerRepository.findAll();
    }

    @Transactional
    public void delete(Integer id) {
        providerRepository.delete(id);
    }

    @Transactional
    public void update(Provider provider){
        providerRepository.update(provider);
    }
}
