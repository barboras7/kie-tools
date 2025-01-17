/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.stunner.core.rule.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jsinterop.annotations.JsType;

@JsType
public final class CanConnect extends AbstractRule {

    public static PermittedConnection buildPermittedConnection(String startRole,
                                                               String endRole) {
        return new PermittedConnection(startRole, endRole);
    }

    @JsType
    public static class PermittedConnection {

        private final String startRole;
        private final String endRole;

        public PermittedConnection(String startRole,
                                   String endRole) {
            this.startRole = startRole;
            this.endRole = endRole;
        }

        /**
         * Role of the start Element that can accept this Connection
         *
         * @return
         */
        public String getStartRole() {
            return startRole;
        }

        /**
         * Role of then end Element that can accept this Connection
         *
         * @return
         */
        public String getEndRole() {
            return endRole;
        }
    }

    private final String role;
    private final List<PermittedConnection> permittedConnections;

    public static CanConnect build(String name,
                                   String role,
                                   PermittedConnection[] permittedConnections) {

        return new CanConnect(name,
                              role,
                              Arrays.stream(permittedConnections).collect(Collectors.toList()));
    }

    public CanConnect(String name,
                      String role,
                      List<PermittedConnection> permittedConnections) {
        super(name);
        this.role = role;
        this.permittedConnections = permittedConnections;
    }

    public String getRole() {
        return role;
    }

    public List<CanConnect.PermittedConnection> getPermittedConnections() {
        return permittedConnections;
    }
}
