package com.algaworks.algashop.ordering.domain.model;

public interface Specification<T> {
    boolean isSatisfiedBy(T t);

    default Specification<T> and(Specification<T> other) {
        return candidate -> this.isSatisfiedBy(candidate) && other.isSatisfiedBy(candidate);
    }

    default Specification<T> or(Specification<T> other) {
        return candidate -> this.isSatisfiedBy(candidate) || other.isSatisfiedBy(candidate);
    }

    default Specification<T> not() {
        return candidate -> !this.isSatisfiedBy(candidate);
    }

    default Specification<T> andNot(final Specification<T> other) {
        return candidate -> this.isSatisfiedBy(candidate) && !other.isSatisfiedBy(candidate);
    }
}
