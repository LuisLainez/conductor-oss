/*
 * Copyright 2022 Conductor Authors.
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
package com.netflix.conductor.client.http;

import java.net.URI;

import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientResponse;

import com.netflix.conductor.common.config.ObjectMapperProvider;
import com.netflix.conductor.common.model.BulkResponse;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.core.MediaType;

public class ClientRequestHandler {
    private final Client client;

    public ClientRequestHandler(ClientConfig config, ClientRequestFilter... filters) {
        ObjectMapper objectMapper = new ObjectMapperProvider().getObjectMapper();

        // https://github.com/FasterXML/jackson-databind/issues/2683
        if (isNewerJacksonVersion()) {
            objectMapper.registerModule(new JavaTimeModule());
        }

        JacksonJsonProvider provider = new JacksonJsonProvider(objectMapper);
        config.register(provider);

        this.client = ClientBuilder.newClient(config);

        for (ClientRequestFilter filter : filters) {
            this.client.register(filter);
        }
    }

    public BulkResponse delete(URI uri, Object body) {
        if (body != null) {
            return client.target(uri)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .delete(BulkResponse.class);
        } else {
            client.target(uri).request().delete();
        }
        return null;
    }

    public ClientResponse get(URI uri) {
        return client.target(uri)
                .request(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN)
                .get(ClientResponse.class);
    }

    public <T> T get(URI uri, Class<T> type) {
        return client.target(uri)
                .request(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN)
                .get(type);
    }

    public <T> T getWithGenericType(URI uri, GenericType<T> type) {
        return client.target(uri)
                .request(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN)
                .get(type);
    }

    public Invocation.Builder getWebResourceBuilder(URI URI) {
        return client.target(URI)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON);
    }

    private boolean isNewerJacksonVersion() {
        Version version = com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION;
        return version.getMajorVersion() == 2 && version.getMinorVersion() >= 12;
    }
}
