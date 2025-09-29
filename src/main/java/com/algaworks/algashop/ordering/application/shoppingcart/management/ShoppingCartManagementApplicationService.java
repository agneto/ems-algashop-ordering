package com.algaworks.algashop.ordering.application.shoppingcart.management;

import com.algaworks.algashop.ordering.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.model.product.Product;
import com.algaworks.algashop.ordering.domain.model.product.ProductCatalogService;
import com.algaworks.algashop.ordering.domain.model.product.ProductId;
import com.algaworks.algashop.ordering.domain.model.product.ProductNotFoundException;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartItemId;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCarts;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingCartManagementApplicationService {
    private final ShoppingCarts shoppingCarts;
    private final ProductCatalogService productCatalogService;
    private final ShoppingService shoppingService;

    @Transactional
    public void addItem(final ShoppingCartItemInput input) {
        Objects.requireNonNull(input);
        final ShoppingCart shoppingCart = this.shoppingCarts.ofId(new ShoppingCartId(input.getShoppingCartId()))
                .orElseThrow(ShoppingCartNotFoundException::new);
        final Product product = this.productCatalogService.ofId(new ProductId(input.getProductId()))
                .orElseThrow(ProductNotFoundException::new);

        shoppingCart.addItem(product, new Quantity(input.getQuantity()));

        this.shoppingCarts.add(shoppingCart);
    }

    @Transactional
    public UUID createNew(final UUID rawCustomerId) {
        Objects.requireNonNull(rawCustomerId);
        final ShoppingCart shoppingCart = this.shoppingService
                .startShopping(new CustomerId(rawCustomerId));

        this.shoppingCarts.add(shoppingCart);

        return shoppingCart.id().value();
    }

    @Transactional
    public void removeItem(final UUID rawShoppingCartId, final UUID rawShoppingCartItemId) {
        Objects.requireNonNull(rawShoppingCartId);
        Objects.requireNonNull(rawShoppingCartItemId);

        final ShoppingCart shoppingCart = this.shoppingCarts.ofId(new ShoppingCartId(rawShoppingCartId))
                .orElseThrow(ShoppingCartNotFoundException::new);

        shoppingCart.removeItem(new ShoppingCartItemId(rawShoppingCartItemId));

        this.shoppingCarts.add(shoppingCart);
    }

    @Transactional
    public void empty(final UUID rawShoppingCartId) {
        final ShoppingCart shoppingCart = this.shoppingCarts.ofId(new ShoppingCartId(rawShoppingCartId))
                .orElseThrow(ShoppingCartNotFoundException::new);

        shoppingCart.empty();

        this.shoppingCarts.add(shoppingCart);
    }

    @Transactional
    public void delete(final UUID rawShoppingCartId) {
        final ShoppingCart shoppingCart = this.shoppingCarts.ofId(new ShoppingCartId(rawShoppingCartId))
                .orElseThrow(ShoppingCartNotFoundException::new);

        this.shoppingCarts.remove(shoppingCart);
    }
}
