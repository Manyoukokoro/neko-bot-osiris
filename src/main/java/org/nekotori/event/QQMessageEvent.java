package org.nekotori.event;

import net.mamoe.mirai.event.events.MessageEvent;
import reactor.core.publisher.Flux;

public class QQMessageEvent<E extends MessageEvent> extends NekoMessageEvent<E> {



    public static <E extends MessageEvent> NekoMessageEvent<E> of(Flux<E> flux){
        NekoMessageEvent<E> eMessageEvent = new QQMessageEvent<>();
        eMessageEvent.flux = flux;
        return eMessageEvent;
    }

    @Override
    public NekoMessageEvent<E> onCommand(String command) {
        this.flux = flux.filter( event->{
            String apply = event.getMessage().contentToString();
            return apply.startsWith(command+" ");
        });
        return this;
    }
}
