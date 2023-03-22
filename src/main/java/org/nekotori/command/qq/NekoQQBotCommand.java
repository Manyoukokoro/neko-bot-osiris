package org.nekotori.command.qq;

import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.MessageEvent;
import org.nekotori.bot.NekoBot;

public interface NekoQQBotCommand {

    void handle(NekoBot<Event, MessageEvent> bot);
}
