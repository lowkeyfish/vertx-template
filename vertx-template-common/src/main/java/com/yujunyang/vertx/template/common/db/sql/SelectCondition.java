/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.db.sql;

import io.vertx.sqlclient.Tuple;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SelectCondition {
    private final List<Filter> filters;
    private final List<Sort> sorts;
    private final Optional<Integer> offset;
    private final Optional<Integer> limit;

    private SelectCondition(Builder b) {
        this.filters = List.copyOf(b.filters);
        this.sorts = List.copyOf(b.sorts);
        this.offset = b.offset;
        this.limit = b.limit;
    }

    public enum Op {
        EQ,
        GT,
        LT,
        GE,
        LE,
        IN,
        LIKE
    }

    public enum Order {
        ASC,
        DESC
    }

    public record Filter(String field, Object value, Op op) {}

    public record Sort(String field, Order order) {}

    public static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private final List<Filter> filters = new ArrayList<>();
        private final List<Sort> sorts = new ArrayList<>();
        private Optional<Integer> offset = Optional.empty();
        private Optional<Integer> limit = Optional.empty();

        public Builder filter(String field, Object value, Op op) {
            filters.add(new Filter(field, value, op));
            return this;
        }

        public Builder sort(String field, Order order) {
            sorts.add(new Sort(field, order));
            return this;
        }

        public Builder offset(int offset) {
            this.offset = Optional.of(offset);
            return this;
        }

        public Builder limit(int limit) {
            this.limit = Optional.of(limit);
            return this;
        }

        public SelectCondition build() {
            return new SelectCondition(this);
        }
    }

    public SqlBuildResult toSql(String baseSql) {
        List<Object> params = new ArrayList<>();
        List<String> whereClauses = new ArrayList<>();

        for (Filter f : filters) {
            String op = operatorToSql(f.op());
            if (f.op() == Op.IN) {
                Collection<?> coll = (Collection<?>) f.value();
                String placeholders = String.join(", ", Collections.nCopies(coll.size(), "?"));
                whereClauses.add(f.field() + " IN (" + placeholders + ")");
                params.addAll(coll);
            } else if (f.op() == Op.LIKE) {
                whereClauses.add(f.field() + " LIKE ?");
                params.add("%" + f.value() + "%");
            } else {
                whereClauses.add(f.field() + " " + op + " ?");
                params.add(f.value());
            }
        }

        StringBuilder sql = new StringBuilder(baseSql);
        if (!whereClauses.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", whereClauses));
        }

        if (!sorts.isEmpty()) {
            String order =
                    sorts.stream().map(s -> s.field() + " " + s.order().name()).collect(Collectors.joining(", "));
            sql.append(" ORDER BY ").append(order);
        }

        limit.ifPresent(l -> {
            sql.append(" LIMIT ?");
            params.add(l);
        });
        offset.ifPresent(o -> {
            sql.append(" OFFSET ?");
            params.add(o);
        });

        return new SqlBuildResult(sql.toString(), Tuple.from(params));
    }

    private static String operatorToSql(Op op) {
        return switch (op) {
            case EQ -> "=";
            case GT -> ">";
            case LT -> "<";
            case GE -> ">=";
            case LE -> "<=";
            case IN -> "IN";
            case LIKE -> "LIKE";
        };
    }

    public record SqlBuildResult(String sql, Tuple tuple) {}
}
