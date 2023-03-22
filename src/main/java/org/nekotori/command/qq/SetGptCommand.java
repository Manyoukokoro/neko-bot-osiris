package org.nekotori.command.qq;

import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import org.nekotori.bot.NekoBot;
import org.nekotori.gpt.GptPersistence;
import org.nekotori.handler.GptGroupMessageHandler;
import org.nekotori.type.QQIdentityType;

public class SetGptCommand implements NekoQQBotCommand {

    GptGroupMessageHandler gptGroupMessageHandler = new GptGroupMessageHandler();

    @Override
    public void handle(NekoBot<Event, MessageEvent> bot){
        bot.onMessageEvent(GroupMessageEvent.class)
                .onCommand("set")
                .onSenderIdentity(QQIdentityType.GROUP_ADMIN)
                .flux()
                .subscribe(event->{
                    String content = event.getMessage().contentToString();
                    String set = content.replaceFirst("set", "").trim();
                    GptPersistence.saveDescription(event.getSubject().getId(),set);
                    gptGroupMessageHandler.resetHardly(event);
                    event.getSubject().sendMessage("success!");
                });
    }
}
