package com.yujunyang.vertx.template.web.infrastructure.event.persistence.db.impl;

import com.yujunyang.vertx.template.common.db.connection.SqlConnectionTemplate;
import com.yujunyang.vertx.template.web.infrastructure.event.persistence.db.EventStoreDataAccessor;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EventStoreDataAccessorImpl implements EventStoreDataAccessor {
    private SqlConnectionTemplate sqlConnectionTemplate;

    @Inject
    public EventStoreDataAccessorImpl(SqlConnectionTemplate sqlConnectionTemplate) {
        this.sqlConnectionTemplate = sqlConnectionTemplate;
    }
}
