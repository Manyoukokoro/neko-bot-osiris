package org.nekotori;

import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import org.nekotori.bot.NekoBot;
import org.nekotori.bot.NekoDiscordBot;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;


public class Application {
    public static void main(String[] args) {
//        NekoBot<BotEvent, MessageEvent> nekoBot = new NekoQQBot();
        NekoBot<Event, MessageCreateEvent> nekoDiscordBot = new NekoDiscordBot();

        nekoDiscordBot.onMessageEvent(MessageCreateEvent.class)
                .onCommand("hello")
                .flux()
                .subscribe(event ->{
                    Mono<MessageChannel> channel = event.getMessage().getChannel();
                    channel.flatMap(ch -> ch.createMessage("world")).subscribe();
                });
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
        interval.blockLast();

//        GptGroupMessageHandler gptGroupMessageHandler = new GptGroupMessageHandler();

//        nekoBot.onMessageEvent(GroupMessageEvent.class)
//                .onCommand("set")
//                .onSenderIdentity(QQIdentityType.GROUP_ADMIN)
//                .flux()
//                .subscribe(event->{
//                    String content = event.getMessage().contentToString();
//                    String set = content.replaceFirst("set", "").trim();
//                    GptPersistence.saveDescription(event.getSubject().getId(),set);
//                    event.getSubject().sendMessage("success!");
//                });
//
//        nekoBot.onMessageEvent(GroupMessageEvent.class)
//                .onCommand("reset")
//                .flux()
//                .subscribe(gptGroupMessageHandler::reset);
//
//        nekoBot.onMessageEvent(GroupMessageEvent.class)
//                .onMessageType(At.class)
//                .onMessageType(event->
//                    event.getMessage().stream().anyMatch(mes->
//                        mes instanceof At && ((At) mes).getTarget() == Long.parseLong(nekoBot.getId())
//                    ))
//                .flux()
//                .subscribe(gptGroupMessageHandler::handle);
    }
}
