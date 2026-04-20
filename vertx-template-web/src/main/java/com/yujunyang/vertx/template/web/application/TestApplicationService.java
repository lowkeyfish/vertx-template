/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.application;

import com.yujunyang.vertx.template.web.domain.test.TestRepository;
import javax.inject.Inject;

public class TestApplicationService {
    private TestRepository testRepository;

    @Inject
    public TestApplicationService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public void test() {}
}
