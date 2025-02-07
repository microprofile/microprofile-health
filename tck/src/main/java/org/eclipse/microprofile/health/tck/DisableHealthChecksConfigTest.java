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

import org.eclipse.microprofile.health.tck.deployment.SuccessfulLiveness;
import org.eclipse.microprofile.health.tck.deployment.SuccessfulReadiness;
import org.eclipse.microprofile.health.tck.deployment.SuccessfulStartup;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.testng.Assert;
import org.testng.annotations.Test;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

/**
 * @author Martin Stefanko
 */
public class DisableHealthChecksConfigTest extends TCKBase {

    private static final String SINGLE_CLASSNAME = "single-classname";
    private static final String MULTIPLE_CLASSNAME = "multiple-classname";

    @Deployment(name = SINGLE_CLASSNAME)
    public static Archive getDeploymentSingleClassnameCheck() {
        return createWarFileWithClasses(SINGLE_CLASSNAME, SuccessfulLiveness.class)
                .addAsManifestResource(
                        new StringAsset("mp.health.disabled.checks=" + SuccessfulLiveness.class.getName()),
                        "microprofile-config.properties");
    }

    /**
     * Verifies that disabling single health check with full class name is respected.
     */
    @Test
    @OperateOnDeployment(SINGLE_CLASSNAME)
    @RunAsClient
    public void testSingleHealthCheckDisabledWithClassname() {
        assertNumberOfChecksPresent(0, "Didn't expect any checks");
    }

    @Deployment(name = MULTIPLE_CLASSNAME)
    public static Archive getDeploymentMultipleClassnameCheck() {
        return createWarFileWithClasses(MULTIPLE_CLASSNAME, SuccessfulLiveness.class, SuccessfulReadiness.class,
                SuccessfulStartup.class)
                .addAsManifestResource(
                        new StringAsset("mp.health.disabled.checks=" + SuccessfulLiveness.class.getName() + ","
                                + SuccessfulReadiness.class.getName()),
                        "microprofile-config.properties");
    }

    /**
     * Verifies that disabling multiple health checks with full class name is respected.
     */
    @Test
    @OperateOnDeployment(MULTIPLE_CLASSNAME)
    @RunAsClient
    public void testMultipleHealthCheckDisabledWithClassname() {
        assertNumberOfChecksPresent(1, "Expected only one check");
    }

    private void assertNumberOfChecksPresent(int expected, String message) {
        Response response = getUrlHealthContents();

        // status code
        Assert.assertEquals(response.getStatus(), 200);

        JsonObject json = readJson(response);

        // response size
        JsonArray checks = json.getJsonArray("checks");
        Assert.assertEquals(checks.size(), expected, message);

        assertOverallSuccess(json);
    }
}
