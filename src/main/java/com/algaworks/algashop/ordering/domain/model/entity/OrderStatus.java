
package com.algaworks.algashop.ordering.domain.model.entity;

import java.util.EnumSet;
import java.util.Set;

public enum OrderStatus {
    DRAFT,
    PLACED,
    PAID,
    READY,
    CANCELED;

    public Set<OrderStatus> nextAllowedStatuses() {
        return switch (this) {
            case DRAFT -> EnumSet.of(PLACED, CANCELED);
            case PLACED -> EnumSet.of(PAID, CANCELED);
            case PAID -> EnumSet.of(READY, CANCELED);
            case READY -> EnumSet.of(CANCELED);
            case CANCELED -> EnumSet.noneOf(OrderStatus.class); // não transita para mais nada
        };
    }

    public boolean canChangeTo(final OrderStatus newStatus) {
        return this.nextAllowedStatuses().contains(newStatus);
    }

    public boolean canNotChangeTo(final OrderStatus newStatus) {
        return !canChangeTo(newStatus);
    }
}
