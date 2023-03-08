package org.nekotori;

import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import org.nekotori.bot.NekoBot;
import org.nekotori.bot.NekoQQBot;
import org.nekotori.gpt.ChatBot;
import org.nekotori.gpt.ChatGpt;
import reactor.core.publisher.Flux;

public class Application {
    public static void main(String[] args) {
        NekoBot<BotEvent> nekoBot = new NekoQQBot();
        Flux<GroupTempMessageEvent> gFlux = nekoBot.listenOn(GroupTempMessageEvent.class);
        Flux<FriendMessageEvent> fFlux = nekoBot.listenOn(FriendMessageEvent.class);
        ChatBot gpt = new ChatGpt();
        gFlux.subscribe(event->{
            String content = event.getMessage().contentToString();
            String reply = gpt.getReply(content);
            event.getSubject().sendMessage(reply);
                });
        fFlux.subscribe(event ->{
            String content = event.getMessage().contentToString();
            String reply = gpt.getReply(content);
            event.getSubject().sendMessage(reply);
        });
    }
}
