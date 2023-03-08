package org.nekotori.bot;

import reactor.core.publisher.Flux;

public interface NekoBot<EVENT_SUPER> {

     <T extends EVENT_SUPER> Flux<T> listenOn(Class<T> eventType);
}
