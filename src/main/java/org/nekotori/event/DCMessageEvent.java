package org.nekotori.event;


import discord4j.core.event.domain.message.MessageEvent;
import reactor.core.publisher.Flux;

public class DCMessageEvent<E extends MessageEvent> extends NekoMessageEvent<E> {

    public static <E extends MessageEvent> NekoMessageEvent<E> of(Flux<E> flux){
        NekoMessageEvent<E> eMessageEvent = new DCMessageEvent<>();
        eMessageEvent.flux = flux;
        return eMessageEvent;
    }

    @Override
    public NekoMessageEvent<E> onCommand(String command) {
        return this;
    }
}
