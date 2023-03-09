package org.nekotori.handler;

import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import net.mamoe.mirai.message.data.SingleMessage;
import org.nekotori.gpt.ChatBot;
import org.nekotori.gpt.ChatGpt;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class GptGroupMessageHandler implements MessageHandler<GroupMessageEvent> {
    private static Map<Member,ChatBot> targetMap = new HashMap<>();

    private static final String ERROR_TEMPLATE = "ops! please contact administrator, the error info is gpt error: %s";
    @Override
    public void handle(GroupMessageEvent message) {
        if(!isAtMe(message)){return;}
        ChatBot chatBot = Optional.ofNullable(targetMap.get(message.getSender()))
                .orElse(new ChatGpt());
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


    private Optional<String> analysisTextContent(MessageChain messages){
        return messages.stream().filter(singleMessage -> !(singleMessage instanceof At))
                .map(SingleMessage::contentToString)
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .filter(Objects::nonNull)
                .reduce(String::concat);
    }

    private boolean isAtMe(GroupMessageEvent message) {
        boolean isAtMe = false;
        for (SingleMessage s : message.getMessage()) {
            if (s instanceof At && ((At) s).getTarget() == message.getBot().getId()) {
                isAtMe = true;
            }
            if (s.contentToString().contains("@" + message.getBot().getNick())) {
                isAtMe = true;
            }
        }
        return isAtMe;
    }
}
