package com.algaworks.algashop.ordering.domain.model.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderStatusTest {

    @Test
    void canChangeTo() {
        Assertions.assertThat(OrderStatus.DRAFT.canChangeTo(OrderStatus.PLACED)).isTrue();
        Assertions.assertThat(OrderStatus.DRAFT.canChangeTo(OrderStatus.CANCELED)).isTrue();
        Assertions.assertThat(OrderStatus.PAID.canChangeTo(OrderStatus.DRAFT)).isFalse();
    }

    @Test
    void canNotChangeTo() {
        Assertions.assertThat(OrderStatus.PLACED.canNotChangeTo(OrderStatus.DRAFT)).isTrue();
    }

    @Test
    void testValidTransitions() {
        assertTrue(OrderStatus.DRAFT.canChangeTo(OrderStatus.PLACED));
        assertTrue(OrderStatus.DRAFT.canChangeTo(OrderStatus.CANCELED));

        assertTrue(OrderStatus.PLACED.canChangeTo(OrderStatus.PAID));
        assertTrue(OrderStatus.PLACED.canChangeTo(OrderStatus.CANCELED));

        assertTrue(OrderStatus.PAID.canChangeTo(OrderStatus.READY));
        assertTrue(OrderStatus.PAID.canChangeTo(OrderStatus.CANCELED));

        assertTrue(OrderStatus.READY.canChangeTo(OrderStatus.CANCELED));
    }

    @Test
    void testInvalidTransitions() {
        assertFalse(OrderStatus.DRAFT.canChangeTo(OrderStatus.PAID));
        assertFalse(OrderStatus.PLACED.canChangeTo(OrderStatus.READY));
        assertFalse(OrderStatus.READY.canChangeTo(OrderStatus.PAID));
        assertFalse(OrderStatus.CANCELED.canChangeTo(OrderStatus.PLACED));
        assertFalse(OrderStatus.CANCELED.canChangeTo(OrderStatus.DRAFT));
    }

    @Test
    void testNoTransitionsFromCanceled() {
        for (OrderStatus status : OrderStatus.values()) {
            assertFalse(OrderStatus.CANCELED.canChangeTo(status));
        }
    }

    @Test
    void testAllowedNextStatuses() {
        assertEquals(Set.of(OrderStatus.PLACED, OrderStatus.CANCELED), OrderStatus.DRAFT.nextAllowedStatuses());
        assertEquals(Set.of(OrderStatus.PAID, OrderStatus.CANCELED), OrderStatus.PLACED.nextAllowedStatuses());
        assertEquals(Set.of(OrderStatus.READY, OrderStatus.CANCELED), OrderStatus.PAID.nextAllowedStatuses());
        assertEquals(Set.of(OrderStatus.CANCELED), OrderStatus.READY.nextAllowedStatuses());
        assertTrue(OrderStatus.CANCELED.nextAllowedStatuses().isEmpty());
    }

}