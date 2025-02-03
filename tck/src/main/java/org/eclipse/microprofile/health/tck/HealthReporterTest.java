/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * See the NOTICES file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */
package org.eclipse.microprofile.health.tck;

import static org.eclipse.microprofile.health.tck.DeploymentUtils.createWarFileWithClasses;
import static org.testng.Assert.assertFalse;

import java.util.function.Supplier;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthReporter;
import org.eclipse.microprofile.health.tck.deployment.SuccessfulLiveness;
import org.eclipse.microprofile.health.tck.deployment.SuccessfulReadiness;
import org.eclipse.microprofile.health.tck.deployment.SuccessfulStartup;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.testng.Assert;
import org.testng.annotations.Test;

import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

/**
 * Verifies the implementation of {@link org.eclipse.microprofile.health.HealthReporter} outputs.
 *
 * @author Martin Stefanko
 */
public class HealthReporterTest extends TCKBase {

    @Inject
    private HealthReporter reporter;

    @Deployment
    public static Archive getDeployment() {
        return createWarFileWithClasses(HealthReporterTest.class.getSimpleName(), SuccessfulLiveness.class,
                SuccessfulReadiness.class, SuccessfulStartup.class);
    }

    /**
     * Verifies default health (all procedures).
     */
    @Test
    public void testHealth() {
        Health health = reporter.getHealth();

        // status code
        assertFalse(health.isDown());

        JsonObject json = health.getPayload();

        // response size
        JsonArray checks = json.getJsonArray("checks");
        Assert.assertEquals(checks.size(), 3, "Expected three check responses");

        // verify that all 3 procedures are present
        for (JsonObject check : checks.getValuesAs(JsonObject.class)) {
            String id = check.getString("name");
            if (id.equals("successful-check")) {
                verifySuccessStatus(check);
            } else {
                Assert.fail("Unexpected response payload structure");
            }
        }

        assertOverallSuccess(json);
    }

    /**
     * Verifies liveness health procedures.
     */
    @Test
    public void testLiveness() {
        verifySingleResponse(() -> reporter.getLiveness());
    }

    /**
     * Verifies readiness health procedures.
     */
    @Test
    public void testReadiness() {
        verifySingleResponse(() -> reporter.getReadiness());
    }

    /**
     * Verifies startup health procedures.
     */
    @Test
    public void testStartup() {
        verifySingleResponse(() -> reporter.getStartup());
    }

    private void verifySingleResponse(Supplier<Health> healthSupplier) {
        Health health = healthSupplier.get();

        // status code
        assertFalse(health.isDown());

        JsonObject json = health.getPayload();

        // response size
        JsonArray checks = json.getJsonArray("checks");
        Assert.assertEquals(checks.size(), 1, "Expected a single check response");

        // single procedure response
        assertSuccessfulCheck(checks.getJsonObject(0), "successful-check");

        assertOverallSuccess(json);
    }

}
