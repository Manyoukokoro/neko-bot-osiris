package org.nekotori.event;

import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import reactor.core.publisher.Flux;


public class QQMessageEvent<E extends MessageEvent> extends NekoMessageEvent<E> {



    public static <E extends MessageEvent> NekoMessageEvent<E> of(Flux<E> flux){
        var eMessageEvent = new QQMessageEvent<E>();
        eMessageEvent.flux = flux;
        return eMessageEvent;
    }

    @Override
    public NekoMessageEvent<E> onCommand(String command) {
        this.flux = flux.filter( event->{
            var apply = event.getMessage().contentToString();
            return apply.startsWith(command+" ");
        });
        return this;
    }

    @Override
    public NekoMessageEvent<E> onVerify(Class<?> clazz) {
        this.flux = flux.filter( event->{
            var message = event.getMessage();
            return message.stream().anyMatch(mes -> mes.getClass() == clazz);
        });
        return this;
    }

}
