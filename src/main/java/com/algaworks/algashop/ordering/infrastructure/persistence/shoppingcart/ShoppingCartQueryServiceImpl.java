package com.algaworks.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.algaworks.algashop.ordering.application.shoppingcart.query.ShoppingCartItemOutput;
import com.algaworks.algashop.ordering.application.shoppingcart.query.ShoppingCartOutput;
import com.algaworks.algashop.ordering.application.shoppingcart.query.ShoppingCartQueryService;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingCartQueryServiceImpl implements ShoppingCartQueryService {

    private final EntityManager entityManager;

    @Override
    public ShoppingCartOutput findById(final UUID shoppingCartId) {
        try {
            TypedQuery<ShoppingCartPersistenceEntity> query = entityManager.createQuery("""
            SELECT DISTINCT s
            FROM ShoppingCartPersistenceEntity s
            LEFT JOIN FETCH s.customer c
            LEFT JOIN FETCH s.items i
            WHERE s.id = :id
        """, ShoppingCartPersistenceEntity.class);
            query.setParameter("id", shoppingCartId);

            ShoppingCartPersistenceEntity entity = query.getSingleResult();

            return toOutput(entity);
        } catch (NoResultException e) {
            throw new ShoppingCartNotFoundException();
        }
    }

    private ShoppingCartOutput toOutput(final ShoppingCartPersistenceEntity entity) {
        return ShoppingCartOutput.builder()
                .id(entity.getId())
                .customerId(entity.getCustomer().getId())
                .totalItems(entity.getTotalItems())
                .totalAmount(entity.getTotalAmount())
                .items(
                        entity.getItems().stream()
                                .map(i -> {
                                    final ShoppingCartItemOutput item = new ShoppingCartItemOutput();
                                    item.setId(i.getId());
                                    item.setProductId(i.getProductId());
                                    item.setName(i.getName());
                                    item.setPrice(i.getPrice());
                                    item.setQuantity(i.getQuantity());
                                    item.setTotalAmount(i.getTotalAmount());
                                    item.setAvailable(i.getAvailable());
                                    return item;
                                })
                                .toList()
                )
                .build();
    }

    @Override
    public ShoppingCartOutput findByCustomerId(final UUID customerId) {
        try {
            TypedQuery<ShoppingCartPersistenceEntity> query = entityManager.createQuery("""
            SELECT DISTINCT s
            FROM ShoppingCartPersistenceEntity s
            LEFT JOIN FETCH s.customer c
            LEFT JOIN FETCH s.items i
            WHERE c.id = :customerId
        """, ShoppingCartPersistenceEntity.class);
            query.setParameter("customerId", customerId);

            ShoppingCartPersistenceEntity entity = query.getSingleResult();

            return toOutput(entity);
        } catch (NoResultException e) {
            throw new ShoppingCartNotFoundException();
        }
    }
}
