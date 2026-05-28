/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.infrastructure.common.persistence;

import com.yujunyang.vertx.template.common.exceptions.ErrorType;
import com.yujunyang.vertx.template.common.exceptions.SystemException;
import java.util.Map;
import java.util.Set;

public interface DataModel {

    /**
     * 对应的表名
     *
     * @return
     */
    String tableName();

    /**
     * 表的所有字段，对应select时期望查询的所有字段
     *
     * @return
     */
    Set<String> columns();

    /**
     * insert使用字段名以及对应的参数值
     *
     * @return
     */
    Map<String, Object> insertValuesParameters();

    /**
     * update set使用的字段名以及对应的参数值
     *
     * <p>不用再返回字段version，如果columns中包含了version字段，update语句会自动生成对应的`version` = `version` + 1
     *
     * <p>如果数据表不涉及到update操作，可以不实现
     *
     * @return
     */
    default Map<String, Object> updateSetParameters() {
        throw new SystemException("表" + tableName() + "不需要update", ErrorType.UNSUPPORTED_OPERATION);
    }

    /**
     * update where使用的字段名以及对应的参数值
     *
     * <p>如果数据表不涉及到update操作，可以不实现
     *
     * @return
     */
    default Map<String, Object> updateWhereParameters() {
        throw new SystemException("表" + tableName() + "不需要update", ErrorType.UNSUPPORTED_OPERATION);
    }
}
