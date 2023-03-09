package org.nekotori.event;

import net.mamoe.mirai.event.events.MessageEvent;
import reactor.core.publisher.Flux;

import java.util.function.Function;

public class QQMessageEvent<E extends MessageEvent> extends NekoMessageEvent<E> {


    public static <E extends MessageEvent> NekoMessageEvent<E> of(Flux<E> flux){
        NekoMessageEvent<E> eMessageEvent = new QQMessageEvent<>();
        eMessageEvent.flux = flux;
        return eMessageEvent;
    }

    @Override
    public Flux<E> onCommand(String command,Function<E,String> messageResolver) {
        return  flux.filter( event->{
            String apply = messageResolver.apply(event);
            return apply.startsWith(command+" ");
        });
    }

    @Override
    public Flux<E> onCommand(String command) {
        return  flux.filter( event->{
            String apply = event.getMessage().contentToString();
            return apply.startsWith(command+" ");
        });
    }
}
