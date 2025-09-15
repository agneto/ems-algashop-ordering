package com.algaworks.algashop.ordering.domain.model.service;

import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerAlreadyHaveShoppingCartException;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.algaworks.algashop.ordering.domain.model.customer.Customers;
import com.algaworks.algashop.ordering.domain.model.repository.ShoppingCarts;
import com.algaworks.algashop.ordering.domain.model.DomainService;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@DomainService
public class ShoppingService {

    private final Customers customers;
    private final ShoppingCarts shoppingCarts;

    public ShoppingCart startShopping(final CustomerId customerId) {

        if (!customers.exists(customerId)) {
            throw new CustomerNotFoundException();
        }

        if (shoppingCarts.ofCustomer(customerId).isPresent()) {
            throw new CustomerAlreadyHaveShoppingCartException();
        }

        return ShoppingCart.startShopping(customerId);
    }
}
