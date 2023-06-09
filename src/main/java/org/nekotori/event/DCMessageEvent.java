package org.nekotori.event;


import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Flux;

public class DCMessageEvent<E extends MessageCreateEvent> extends NekoMessageEvent<E> {

    public static <E extends MessageCreateEvent> NekoMessageEvent<E> of(Flux<E> flux){
        var eMessageEvent = new DCMessageEvent<E>();
        eMessageEvent.flux = flux;
        return eMessageEvent;
    }

    @Override
    public NekoMessageEvent<E> onCommand(String command) {
        this.flux = flux.filter(event ->{
                    Message message = event.getMessage();
                    String content = message.getContent();
                    return content.startsWith(command +" ");
                });
        return this;
    }

    @Override
    public NekoMessageEvent<E> onVerify(Class<?> clazz) {
        this.flux = flux.filter(event -> event.getClass() == clazz);
        return this;
    }
}
