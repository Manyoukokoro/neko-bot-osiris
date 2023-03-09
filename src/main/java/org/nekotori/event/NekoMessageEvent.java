package org.nekotori.event;

import reactor.core.publisher.Flux;

import java.util.function.Function;

public abstract class NekoMessageEvent<E> {

    protected Flux<E> flux;

    public Flux<E> flux() {
        return flux;
    }

    public abstract Flux<E> onCommand(String command, Function<E, String> messageResolver);

    public abstract Flux<E> onCommand(String command);
}
