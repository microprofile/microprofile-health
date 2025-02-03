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

import java.util.Objects;

import jakarta.json.JsonObject;

public class Health {

    private JsonObject payload;

    public Health(JsonObject payload) {
        this.payload = payload;
    }

    public JsonObject getPayload() {
        return payload;
    }

    public boolean isDown() {
        return HealthCheckResponse.Status.DOWN.toString().equals(payload.getString("status"));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Health))
            return false;
        Health health = (Health) o;
        return Objects.equals(getPayload(), health.getPayload());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getPayload());
    }
}
