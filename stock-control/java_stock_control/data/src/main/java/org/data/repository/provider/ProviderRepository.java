package org.data.repository.provider;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TransactionRequiredException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.domain.provider.Provider;
import org.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProviderRepository {
    private final EntityManager entityManager;

    @Autowired
    public ProviderRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Transactional
    public Provider createProvider(Provider provider){
        try {
            this.entityManager.persist(provider);
            return provider;
        }
        catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    public List<Provider> findAll(){
        try{
            String queryString = "SELECT p FROM Provider p";
            TypedQuery<Provider> query = entityManager.createQuery(queryString, Provider.class);
            return query.getResultList();
        }catch (TransactionRequiredException e) {
            throw new TransactionRequiredException(e.getMessage(), e);
        }
    }

    public Provider findProvider(String cuit){
        try {
            String queryString = "SELECT p FROM Provider p WHERE p.cuit=:cuit";
            TypedQuery<Provider> query = entityManager.createQuery(queryString, Provider.class);
            query.setParameter("cuit", cuit);
            return query.getSingleResult();
        } catch (TransactionRequiredException e) {
            throw new TransactionRequiredException(e.getMessage(), e);
        }
    }
}
