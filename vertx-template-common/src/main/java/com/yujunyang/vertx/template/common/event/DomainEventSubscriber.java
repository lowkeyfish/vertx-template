/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.event;

import io.vertx.core.Future;

public interface DomainEventSubscriber {

    Future<Void> handleEvents();
}
