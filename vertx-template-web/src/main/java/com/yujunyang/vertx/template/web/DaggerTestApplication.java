/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web;

import com.yujunyang.vertx.template.web.application.TestApplicationService;
import com.yujunyang.vertx.template.web.di.AppComponent;
import com.yujunyang.vertx.template.web.di.DaggerAppComponent;

public class DaggerTestApplication {
    public static void main(String[] args) {
        AppComponent appComponent = DaggerAppComponent.create();
        TestApplicationService testApplicationService = appComponent.getTestApplicationService();
        testApplicationService.test();
    }
}
