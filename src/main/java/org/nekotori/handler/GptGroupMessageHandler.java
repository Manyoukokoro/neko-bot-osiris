package org.nekotori.handler;

import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import net.mamoe.mirai.message.data.SingleMessage;
import org.nekotori.gpt.ChatBot;
import org.nekotori.gpt.ChatGpt;
import org.nekotori.gpt.GptPersistence;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class GptGroupMessageHandler extends GroupAtMeHandler {
    private static Map<Member,ChatBot> targetMap = new HashMap<>();

    private static final String ERROR_TEMPLATE = "ops! please contact administrator, the error info is gpt error: %s";
    @Override
    public void handle(GroupMessageEvent message) {
        String description = GptPersistence.getDescription(message.getGroup().getId());
        ChatBot chatBot = Optional.ofNullable(targetMap.get(message.getSender()))
                .orElse(new ChatGpt(description));
        Optional<String> s = analysisTextContent(message.getMessage());
        s.ifPresent( content-> chatBot.getReply(content)
                .contextWrite((ctx)->ctx.put(ChatBot.ERROR_KEY,ERROR_TEMPLATE))
                .subscribe(reply-> message.getSubject().sendMessage(new MessageChainBuilder()
                        .append(new QuoteReply(message.getMessage()))
                        .append(reply)
                        .build()))
        );
        targetMap.putIfAbsent(message.getSender(),chatBot);
    }


    public void reset(GroupMessageEvent event){
        Member sender = event.getSender();
        Optional<ChatBot> botOp = Optional.ofNullable(targetMap.get(sender));
        botOp.ifPresent(ChatBot::refresh);
    }

    public void resetHardly(GroupMessageEvent event){
        Member sender = event.getSender();
        targetMap.remove(sender);
    }


    private Optional<String> analysisTextContent(MessageChain messages){
        List<String> prom = messages.stream()
                .map(SingleMessage::contentToString)
                .map(String::trim)
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .filter(Objects::nonNull)
                .filter(s->!s.isEmpty())
                .collect(Collectors.toList());
        // this is command
        prom.remove(0);
        return prom.stream()
                .reduce(String::concat);
    }

}
