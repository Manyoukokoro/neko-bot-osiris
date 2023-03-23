package org.nekotori.resource;

import reactor.netty.http.client.HttpClient;

import java.util.function.Supplier;

public class ReactorResources {
    public static final Supplier<HttpClient> DEFAULT_HTTP_CLIENT =
            () -> HttpClient.create().compress(true).followRedirect(true);
}
