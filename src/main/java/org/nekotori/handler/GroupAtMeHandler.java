package org.nekotori.handler;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import org.nekotori.bot.NekoBot;

public abstract class GroupAtMeHandler implements MessageHandler<GroupMessageEvent> {

    @Override
    public <B,M> boolean  verify(GroupMessageEvent message, NekoBot<B,M> bot) {
        return message.getMessage().stream().anyMatch(mes ->
                mes instanceof At && ((At) mes).getTarget() == Long.parseLong(bot.getId())
        );
    }
}
