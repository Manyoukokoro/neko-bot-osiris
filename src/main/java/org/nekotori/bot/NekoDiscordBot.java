package org.nekotori.bot;

import discord4j.core.event.domain.Event;
import reactor.core.publisher.Flux;

public class NekoDiscordBot implements NekoBot<Event> {
    @Override
    public <T extends Event> Flux<T> listenOn(Class<T> eventType) {
        return null;
    }
}
