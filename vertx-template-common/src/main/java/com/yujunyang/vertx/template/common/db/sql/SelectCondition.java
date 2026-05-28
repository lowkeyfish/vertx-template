/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.db.sql;

import io.vertx.sqlclient.Tuple;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 动态 SQL 查询条件构建器，用于生成安全的预编译 SQL 语句（使用 {@code ?} 占位符）及对应的 {@link io.vertx.sqlclient.Tuple} 参数。
 *
 * <p>本类采用 Builder 模式，支持：
 *
 * <ul>
 *   <li>等值、不等值（{@literal >, <, >=, <=}）、{@code IN}、{@code LIKE} 等条件
 *   <li>条件组合（AND / OR），支持任意嵌套
 *   <li>排序（ORDER BY）和分页（LIMIT / OFFSET）
 * </ul>
 *
 * <h2>基本用法示例</h2>
 *
 * <h3>1. 简单等值条件</h3>
 *
 * <pre>{@code
 * SelectCondition cond = SelectCondition.builder()
 *     .filter(AccountTable.ID, 100L, Op.EQ)
 *     .filter(AccountTable.STATUS, 1, Op.EQ)
 *     .build();
 * // 生成 SQL: ... WHERE id = ? AND status = ?
 * }</pre>
 *
 * <h3>2. 范围查询（大于、小于等于）</h3>
 *
 * <pre>{@code
 * SelectCondition cond = SelectCondition.builder()
 *     .filter(AccountTable.AGE, 18, Op.GT)
 *     .filter(AccountTable.CREATE_TIME, startTime, Op.GE)
 *     .filter(AccountTable.CREATE_TIME, endTime, Op.LE)
 *     .build();
 * }</pre>
 *
 * <h3>3. IN 查询</h3>
 *
 * <pre>{@code
 * List<Long> ids = List.of(101L, 102L, 103L);
 * SelectCondition cond = SelectCondition.builder()
 *     .filter(AccountTable.ID, ids, Op.IN)
 *     .build();
 * // 生成: ... WHERE id IN (?, ?, ?)
 * }</pre>
 *
 * <h3>4. LIKE 模糊查询</h3>
 *
 * <pre>{@code
 * SelectCondition cond = SelectCondition.builder()
 *     .filter(AccountTable.USERNAME, "john", Op.LIKE)
 *     .build();
 * // 生成: ... WHERE username LIKE ?  参数值自动添加 %john% (由构建器负责)
 * }</pre>
 *
 * <h3>5. 排序 + 分页</h3>
 *
 * <pre>{@code
 * SelectCondition cond = SelectCondition.builder()
 *     .filter(AccountTable.STATUS, 1, Op.EQ)
 *     .sort(AccountTable.CREATE_TIME, Order.DESC)
 *     .limit(20)
 *     .offset(40)
 *     .build();
 * // 生成: ... WHERE status = ? ORDER BY create_time DESC LIMIT ? OFFSET ?
 * }</pre>
 *
 * <h3>6. OR 组合条件（顶层无括号）</h3>
 *
 * <pre>{@code
 * SelectCondition cond = SelectCondition.builder()
 *     .or(b -> b
 *         .filter(AccountTable.EMAIL, "admin@example.com", Op.EQ)
 *         .filter(AccountTable.MOBILE, "13800000000", Op.EQ)
 *     )
 *     .build();
 * // 生成: ... WHERE email = ? OR mobile = ?
 * }</pre>
 *
 * <h3>7. 混合 AND / OR 嵌套</h3>
 *
 * <pre>{@code
 * SelectCondition cond = SelectCondition.builder()
 *     .filter(AccountTable.STATUS, 1, Op.EQ)
 *     .or(b -> b
 *         .filter(AccountTable.ROLE, "admin", Op.EQ)
 *         .and(c -> c
 *             .filter(AccountTable.CREATE_TIME, start, Op.GE)
 *             .filter(AccountTable.CREATE_TIME, end, Op.LE)
 *         )
 *     )
 *     .build();
 * // 生成: ... WHERE status = ? AND (role = ? OR (create_time >= ? AND create_time <= ?))
 * }</pre>
 *
 * <h2>与 Vert.x SQL Client 集成</h2>
 *
 * <pre>{@code
 * SqlBuildResult result = cond.toSql("SELECT id, name FROM account");
 * client.preparedQuery(result.sql())
 *       .execute(result.tuple())
 *       .onSuccess(rows -> { ... });
 * }</pre>
 *
 * <p><b>注意：</b> 所有 {@code filter} 方法中的 {@code value} 参数会原样存入 Tuple，对于 {@code IN} 操作必须传入 {@link java.util.Collection}
 * 类型。
 *
 * @see #builder()
 * @see #toSql(String)
 */
public class SelectCondition {
    private static final String SQL_SELECT_BASE_PATTERN = """
        SELECT {0} FROM `{1}`
        """;
    private static final String SQL_COUNT_BASE_PATTERN = """
        SELECT COUNT(1) AS `count` FROM `{0}`
        """;
    private static final String SQL_DELETE_BASE_PATTERN = """
        DELETE FROM `{0}`
        """;
    private static final String SQL_LOGIC_DELETE_BASE_PATTERN = """
        UPDATE `{0}` SET `deleted` = UNIX_TIMESTAMP(), `update_time` = CURRENT_TIMESTAMP(), `version` = `version` + 1
        """;

    private final String tableName;
    private final Set<String> columns;
    private final List<Filter> filters;
    private final List<Sort> sorts;
    private final Optional<Integer> offset;
    private final Optional<Integer> limit;

    private SelectCondition(Builder b) {
        this.filters = List.copyOf(b.filters);
        this.sorts = List.copyOf(b.sorts);
        this.offset = b.offset;
        this.limit = b.limit;
        this.tableName = b.tableName;
        this.columns = b.columns;
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

    public sealed interface Filter permits AtomicFilter, FilterGroup {}

    public record AtomicFilter(String field, Object value, Op op) implements Filter {}

    public record FilterGroup(List<Filter> filters, Logic logic) implements Filter {
        public enum Logic {
            AND,
            OR
        }
    }

    public record Sort(String field, Order order) {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String tableName = "";
        private Set<String> columns = new HashSet<>();
        private final List<Filter> filters = new ArrayList<>();
        private final List<Sort> sorts = new ArrayList<>();
        private Optional<Integer> offset = Optional.empty();
        private Optional<Integer> limit = Optional.empty();

        public Builder tableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public Builder columns(Set<String> columns) {
            this.columns = new HashSet<>(columns);
            return this;
        }

        public Builder filter(String field, Object value, Op op) {
            filters.add(new AtomicFilter(field, value, op));
            return this;
        }

        // 开始一个 OR 组
        public Builder or(Consumer<Builder> groupBuilder) {
            Builder inner = new Builder();
            groupBuilder.accept(inner);
            if (!inner.filters.isEmpty()) {
                filters.add(new FilterGroup(new ArrayList<>(inner.filters), FilterGroup.Logic.OR));
            }
            return this;
        }

        // 开始一个 AND 组
        public Builder and(Consumer<Builder> groupBuilder) {
            Builder inner = new Builder();
            groupBuilder.accept(inner);
            if (!inner.filters.isEmpty()) {
                filters.add(new FilterGroup(new ArrayList<>(inner.filters), FilterGroup.Logic.AND));
            }
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

    private void appendFilter(StringBuilder sql, List<Object> params, Filter filter, boolean wrapInParentheses) {
        if (filter instanceof AtomicFilter af) {
            // 原子条件不加括号
            String op = operatorToSql(af.op());
            if (af.op() == Op.IN) {
                Collection<?> coll = (Collection<?>) af.value();
                String placeholders = String.join(", ", Collections.nCopies(coll.size(), "?"));
                sql.append(af.field()).append(" IN (").append(placeholders).append(")");
                params.addAll(coll);
            } else if (af.op() == Op.LIKE) {
                sql.append(af.field()).append(" LIKE ?");
                params.add("%" + af.value() + "%");
            } else {
                sql.append(af.field()).append(" ").append(op).append(" ?");
                params.add(af.value());
            }
        } else if (filter instanceof FilterGroup fg) {
            if (wrapInParentheses) sql.append("(");
            for (int i = 0; i < fg.filters().size(); i++) {
                if (i > 0) sql.append(" ").append(fg.logic().name()).append(" ");
                // 组内子条件需要括号（如有嵌套）
                appendFilter(sql, params, fg.filters().get(i), true);
            }
            if (wrapInParentheses) sql.append(")");
        }
    }

    public SqlBuildResult toSql(String baseSql) {
        List<Object> params = new ArrayList<>();
        List<String> whereClauses = new ArrayList<>();

        for (Filter filter : filters) {
            StringBuilder clause = new StringBuilder();
            // 顶层只有一个 Filter 且是 FilterGroup 时不加外层括号
            boolean isSingleGroup = (filters.size() == 1 && filter instanceof FilterGroup);
            appendFilter(clause, params, filter, isSingleGroup);
            whereClauses.add(clause.toString());
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

    public SqlBuildResult toSelectSql() {
        return toSql(MessageFormat.format(
                SQL_SELECT_BASE_PATTERN,
                String.join(", ", columns.stream().map(n -> "`" + n + "`").toList()),
                tableName));
    }

    public SqlBuildResult toCountSql() {
        return toSql(MessageFormat.format(SQL_COUNT_BASE_PATTERN, tableName));
    }

    public SqlBuildResult toDeleteSql() {
        return toSql(MessageFormat.format(SQL_DELETE_BASE_PATTERN, tableName));
    }

    public SqlBuildResult toLogicDeleteSql() {
        return toSql(MessageFormat.format(SQL_LOGIC_DELETE_BASE_PATTERN, tableName));
    }

    private static String operatorToSql(SelectCondition.Op op) {
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
