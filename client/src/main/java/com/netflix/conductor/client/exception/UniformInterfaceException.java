package com.netflix.conductor.client.exception;


import org.glassfish.jersey.client.ClientResponse;

/**
 * This is a convenience class to capture response codes that are less than 200
 * It was deprecated after Jersey1, but in order to avoid a bigger refactor, we keep something
 * similar
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

    public UniformInterfaceException(String message, ClientResponse r, boolean bufferResponseEntity) {
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
