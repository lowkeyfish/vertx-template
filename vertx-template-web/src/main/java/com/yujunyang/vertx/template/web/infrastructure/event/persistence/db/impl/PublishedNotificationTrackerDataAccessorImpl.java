package com.yujunyang.vertx.template.web.infrastructure.event.persistence.db.impl;

import com.yujunyang.vertx.template.common.db.connection.SqlConnectionTemplate;
import com.yujunyang.vertx.template.web.infrastructure.event.persistence.db.PublishedNotificationTrackerDataAccessor;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PublishedNotificationTrackerDataAccessorImpl implements PublishedNotificationTrackerDataAccessor {
    private SqlConnectionTemplate sqlConnectionTemplate;

    @Inject
    public PublishedNotificationTrackerDataAccessorImpl(SqlConnectionTemplate sqlConnectionTemplate) {
        this.sqlConnectionTemplate = sqlConnectionTemplate;
    }
}
