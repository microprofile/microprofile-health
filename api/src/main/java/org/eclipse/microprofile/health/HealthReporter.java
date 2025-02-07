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
package org.eclipse.microprofile.health;

/**
 *
 * CDI injectable interface that allows accessing of the health information
 *
 * @author Martin Stefanko
 * @since 4.1
 */
public interface HealthReporter {

    /**
     * The result of calling all available health checks.
     *
     * @return {@link Health} representing all health check procedures.
     */
    Health getHealth();


    /**
     * The result of calling all liveness health checks.
     *
     * @return {@link Health} representing only the liveness health check procedures.
     */
    Health getLiveness();


    /**
     * The result of calling all readiness health checks.
     *
     * @return {@link Health} representing only the readiness health check procedures.
     */
    Health getReadiness();

    /**
     * The result of calling all startup health checks.
     *
     * @return {@link Health} representing only the startup health check procedures.
     */
    Health getStartup();

}
