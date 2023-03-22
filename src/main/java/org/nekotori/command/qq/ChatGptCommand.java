package org.nekotori.command.qq;

import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import org.nekotori.bot.NekoBot;
import org.nekotori.handler.GptGroupMessageHandler;

public class ChatGptCommand implements NekoQQBotCommand {
    GptGroupMessageHandler gptGroupMessageHandler = new GptGroupMessageHandler();
    @Override
    public void handle(NekoBot<Event, MessageEvent> bot) {
        bot.onMessageEvent(GroupMessageEvent.class)
                .onVerify(event->
                        event.getMessage().stream().anyMatch(mes->
                                mes instanceof At && ((At) mes).getTarget() == Long.parseLong(bot.getId())
                        ))
                .flux()
                .subscribe(gptGroupMessageHandler::handle);
    }
}
