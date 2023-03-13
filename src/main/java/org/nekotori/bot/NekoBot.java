package org.nekotori.bot;

import org.nekotori.event.NekoMessageEvent;
import reactor.core.publisher.Flux;

public interface NekoBot<B,M> {

     <T extends B> Flux<T> onEvent(Class<T> eventType);

     <T extends M> NekoMessageEvent<T> onMessageEvent(Class<T> eventType);

     String getId();
}
