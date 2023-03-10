package org.nekotori;

import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import org.nekotori.bot.NekoBot;
import org.nekotori.bot.NekoQQBot;
import org.nekotori.gpt.GptPersistence;
import org.nekotori.handler.GptGroupMessageHandler;
import org.nekotori.type.QQIdentityType;

public class Application {
    public static void main(String[] args) {
        NekoBot<BotEvent, MessageEvent> nekoBot = new NekoQQBot();

        nekoBot.onMessageEvent(GroupMessageEvent.class)
                .onCommand("set")
                .onSenderIdentity(QQIdentityType.GROUP_ADMIN)
                .flux()
                .subscribe(event->{
                    String content = event.getMessage().contentToString();
                    String set = content.replaceFirst("set", "").trim();
                    GptPersistence.saveDescription(event.getSubject().getId(),set);
                    event.getSubject().sendMessage("success!");
                });


        nekoBot.onMessageEvent(GroupMessageEvent.class)
                .onMessageType(At.class)
                .onMessageType(event->
                    event.getMessage().stream().anyMatch(mes->
                        mes instanceof At && ((At) mes).getTarget() == Long.parseLong(nekoBot.getId())
                    ))
                .flux()
                .subscribe(event-> new GptGroupMessageHandler().handle(event));
    }
}
