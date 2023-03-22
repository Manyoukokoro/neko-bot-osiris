package org.nekotori.handler;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.nekotori.bot.NekoBot;

public interface MessageHandler<T> {
    void handle(T message);

    <B,M> boolean  verify(GroupMessageEvent message, NekoBot<B,M> bot);
}
