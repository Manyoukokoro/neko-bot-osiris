package org.nekotori.handler;

import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import net.mamoe.mirai.message.data.SingleMessage;
import org.nekotori.gpt.ChatBot;
import org.nekotori.gpt.ChatGpt;
import org.nekotori.persistence.GptPersistence;

import java.util.*;
import java.util.stream.Collectors;

public class GptGroupMessageHandler extends GroupAtMeHandler {
    private static final Map<Member,ChatBot> targetMap = new HashMap<>();

    private static final String ERROR_TEMPLATE = "ops! please contact administrator, the error info is gpt error: %s";
    @Override
    public void handle(GroupMessageEvent message) {
        var description = GptPersistence.getDescription(message.getGroup().getId());
        var chatBot = Optional.ofNullable(targetMap.get(message.getSender()))
                .orElse(new ChatGpt(description));
        var s = analysisTextContent(message.getMessage());
        s.ifPresent( content-> chatBot.getReply(content)
                .contextWrite((ctx)->ctx.put(ChatBot.ERROR_KEY,ERROR_TEMPLATE))
                .subscribe(reply-> message.getSubject().sendMessage(new MessageChainBuilder()
                        .append(new QuoteReply(message.getMessage()))
                        .append(reply)
                        .build()))
        );
        targetMap.putIfAbsent(message.getSender(),chatBot);
    }

    public void setGptCharacter(GroupMessageEvent event) {
        var content = event.getMessage().contentToString();
        var set = content.replaceFirst("set", "").trim();
        GptPersistence.saveDescription(event.getSubject().getId(),set);
        resetHardly(event);
        event.getSubject().sendMessage("success!");
    }


    public void reset(GroupMessageEvent event){
        var sender = event.getSender();
        var botOp = Optional.ofNullable(targetMap.get(sender));
        botOp.ifPresent(ChatBot::refresh);
    }




    private void resetHardly(GroupMessageEvent event){
        var sender = event.getSender();
        targetMap.remove(sender);
    }


    private Optional<String> analysisTextContent(MessageChain messages){
        var prom = messages.stream()
                .map(SingleMessage::contentToString)
                .map(String::trim)
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .filter(Objects::nonNull)
                .filter(s->!s.isEmpty())
                .collect(Collectors.toList());
        // this is command
        prom.remove(0);
        return prom.stream().reduce(String::concat);
    }

}
