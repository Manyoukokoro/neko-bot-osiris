package org.nekotori;

import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.nekotori.bot.NekoBot;
import org.nekotori.bot.NekoQQBot;
import org.nekotori.handler.GptGroupMessageHandler;

public class Application {
    public static void main(String[] args) {
        NekoBot<BotEvent> nekoBot = new NekoQQBot();



        nekoBot.listenOn(GroupMessageEvent.class)
                .subscribe(event-> new GptGroupMessageHandler().handle(event));
    }
}
