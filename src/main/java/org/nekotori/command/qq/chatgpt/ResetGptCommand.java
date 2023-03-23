package org.nekotori.command.qq.chatgpt;

import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import org.nekotori.bot.NekoBot;
import org.nekotori.command.qq.NekoQQBotCommand;
import org.nekotori.handler.GptGroupMessageHandler;

public class ResetGptCommand implements NekoQQBotCommand {

    GptGroupMessageHandler gptGroupMessageHandler = new GptGroupMessageHandler();

    @Override
    public void handle(NekoBot<Event, MessageEvent> bot) {
        bot.onMessageEvent(GroupMessageEvent.class)
                .onCommand("reset")
                .flux()
                .subscribe(gptGroupMessageHandler::reset);
    }
}
