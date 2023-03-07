package org.nekotori;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import reactor.core.publisher.Flux;

public class Application {

    public static void main(String[] args) {
        NekoBot nekoBot = new NekoBot();
        Flux<GroupMessageEvent> flux = nekoBot.getFlux(GroupMessageEvent.class);
        flux.subscribe(event -> System.out.println(event.getMessage().contentToString()));
        flux.blockLast();
    }
}
