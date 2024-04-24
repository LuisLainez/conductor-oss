/*
 * Copyright 2024 Conductor Authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.netflix.conductor.client.exception;

import org.glassfish.jersey.client.ClientResponse;

/**
 * This is a convenience class to capture response codes that are less than 200 It was deprecated
 * after Jersey1, but in order to avoid a bigger refactor, we keep something similar
 */
public class UniformInterfaceException extends RuntimeException {
    private final transient ClientResponse r;

    public UniformInterfaceException(ClientResponse r) {
        this(r, true);
    }

    public UniformInterfaceException(ClientResponse r, boolean bufferResponseEntity) {
        super(r.toString());
        if (bufferResponseEntity) {
            r.bufferEntity();
        }

        this.r = r;
    }

    public UniformInterfaceException(String message, ClientResponse r) {
        this(message, r, true);
    }

    public UniformInterfaceException(
            String message, ClientResponse r, boolean bufferResponseEntity) {
        super(message);
        if (bufferResponseEntity) {
            r.bufferEntity();
        }

        this.r = r;
    }

    public ClientResponse getResponse() {
        return this.r;
    }
}
