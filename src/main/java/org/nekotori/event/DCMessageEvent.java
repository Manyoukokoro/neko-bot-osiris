package org.nekotori.event;


import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.message.MessageEvent;
import reactor.core.publisher.Flux;

import java.util.function.Function;

public class DCMessageEvent<E extends MessageEvent> extends NekoMessageEvent<E> {

    public static <E extends MessageEvent> NekoMessageEvent<E> of(Flux<E> flux){
        NekoMessageEvent<E> eMessageEvent = new DCMessageEvent<>();
        eMessageEvent.flux = flux;
        return eMessageEvent;
    }
    @Override
    public Flux<E> onCommand(String command, Function<E, String> messageResolver) {
        return null;
    }

    @Override
    public Flux<E> onCommand(String command) {
        return null;
    }
}
