package com.nikita.shop.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;

public class QPredicate {
    private final List<Predicate> predicates = new ArrayList<>();

    private QPredicate() {
    }

    public static QPredicate builder() {
        return new QPredicate();
    }

    public <T> QPredicate add(T object, Function<T, Predicate> function) {
        if (object != null) {
            predicates.add(function.apply(object));
        }
        return this;
    }

    public Predicate buildAnd() {
        return ExpressionUtils.allOf(predicates);
    }

    public Predicate buildOr() {
        return ExpressionUtils.anyOf(predicates);
    }
}
