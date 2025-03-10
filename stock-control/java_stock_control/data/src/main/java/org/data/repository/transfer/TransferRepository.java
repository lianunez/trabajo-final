package org.data.repository.transfer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.domain.transfer.Transfer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class TransferRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    public TransferRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Transfer createTransfer(Transfer transfer) {
        try {
            Objects.requireNonNull(transfer);
            entityManager.persist(transfer);
            entityManager.flush();
            return transfer;
        } catch (Exception e) {
            throw new RuntimeException("Error inserting transfer: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deleteTransfer(String id) {
        try {
            Transfer foundTransfer = entityManager.find(Transfer.class, String.valueOf(id));
            if (foundTransfer != null) {
                entityManager.remove(foundTransfer);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error removing transfer: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void updateTransfer(Transfer transfer) {
        try {
            Objects.requireNonNull(transfer);
            entityManager.merge(transfer);
        } catch (Exception e) {
            throw new RuntimeException("Error updating transfer: " + e.getMessage(), e);
        }
    }

    public Transfer getTransferById(Integer id) {
        try {
            Objects.requireNonNull(id);
            return entityManager.find(Transfer.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching transfer: " + e.getMessage(), e);
        }
    }

    public List<Transfer> getTransfers() {
        try {
            String sql = "SELECT t FROM Transfer t " +
                    "JOIN t.user u " +
                    "JOIN t.transfers pht " +
                    "JOIN pht.product p";

            TypedQuery<Transfer> transfers = entityManager.createQuery(sql, Transfer.class);
            return transfers.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching transfer: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void createProductTransfer(Transfer transfer) {
        try {
            Objects.requireNonNull(transfer);
            transfer.getTransfers().stream()
                    .forEach(productHasTransfer -> {
                        entityManager.persist(productHasTransfer);
                    });
        } catch (Exception e) {
            throw new RuntimeException("Error inserting product transfers: " + e.getMessage(), e);
        }
    }
}
