package org.nekotori.event;

import reactor.core.publisher.Flux;

import java.util.function.Predicate;

public abstract class NekoMessageEvent<E> {

    protected Flux<E> flux;

    public Flux<E> flux() {
        return flux;
    }
    public abstract NekoMessageEvent<E> onCommand(String command);

    public abstract NekoMessageEvent<E> onMessageType(Class<?> clazz);

    public NekoMessageEvent<E> onCommand(Predicate<E> commandSelector){
        this.flux = flux.filter(commandSelector);
        return this;
    }

    public NekoMessageEvent<E> onSenderIdentity(Predicate<E> senderIdentitySelector){
        this.flux = flux.filter(senderIdentitySelector);
        return this;
    }

    public NekoMessageEvent<E> onMessageType(Predicate<E> messageTypeSelector){
        this.flux = flux.filter(messageTypeSelector);
        return this;
    }


}
