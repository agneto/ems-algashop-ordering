package com.algaworks.algashop.ordering.domain.valueobject;

import java.util.Objects;

public record LoyaltyPoints(Integer value) implements Comparable<LoyaltyPoints> {

    public LoyaltyPoints() {
        this(0);
    }

    public LoyaltyPoints(final Integer value) {
        Objects.requireNonNull(value);
        if (value < 0) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    public LoyaltyPoints add(final Integer value) {
        return add(new LoyaltyPoints(value));
    }

    public LoyaltyPoints add(final LoyaltyPoints loyaltyPoints) {
        Objects.requireNonNull(loyaltyPoints);
        if (loyaltyPoints.value() < 0) {
            throw new IllegalArgumentException();
        }

        return new LoyaltyPoints(this.value() + loyaltyPoints.value());
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int compareTo(final LoyaltyPoints o) {
        return this.value().compareTo(o.value());
    }
}
