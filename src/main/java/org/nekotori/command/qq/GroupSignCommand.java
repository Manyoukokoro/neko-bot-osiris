package org.nekotori.command.qq;

import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import org.nekotori.bot.NekoBot;

public class GroupSignCommand implements NekoQQBotCommand {
    @Override
    public void handle(NekoBot<Event, MessageEvent> bot) {
        bot.onMessageEvent(GroupMessageEvent.class)
                .onCommand("签到")
                .flux()
                .subscribe();
    }
}
