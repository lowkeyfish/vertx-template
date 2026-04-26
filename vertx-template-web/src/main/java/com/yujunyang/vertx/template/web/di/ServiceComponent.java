/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.di;

import com.yujunyang.vertx.template.web.application.TestApplicationService;
import dagger.Subcomponent;

@Subcomponent
public interface ServiceComponent {
    @Subcomponent.Factory
    interface Factory {
        ServiceComponent create();
    }

    TestApplicationService getTestApplicationService();
}
