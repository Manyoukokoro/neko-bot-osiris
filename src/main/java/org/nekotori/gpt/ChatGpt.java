package org.nekotori.gpt;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.nekotori.config.FileBasedBotConfiguration;
import org.nekotori.log.TerminalLogger;
import reactor.core.publisher.Flux;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.LinkedList;
import java.util.Optional;

public class ChatGpt implements ChatBot {

    private static final String END_POINT = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL = "model";
    private static final String MODEL_VALUE = "gpt-3.5-turbo";
    private static final String MESSAGE = "messages";
    private final LinkedList<HISTORY> history;

    @Setter
    private String description;
    private final String privateKey;
    private final String proxyHost;
    private final int proxyPort;

    public ChatGpt(String description){
        history = new LinkedList<>();
        privateKey = FileBasedBotConfiguration.getINSTANCE().getGptKey();
        proxyHost = FileBasedBotConfiguration.getINSTANCE().getDiscord().getProxyHost();
        proxyPort = FileBasedBotConfiguration.getINSTANCE().getDiscord().getProxyPort();
        this.description = description;
    }

    private ChatGpt(String key,String description){
        history = new LinkedList<>();
        privateKey = key;
        proxyHost = FileBasedBotConfiguration.getINSTANCE().getDiscord().getProxyHost();
        proxyPort = FileBasedBotConfiguration.getINSTANCE().getDiscord().getProxyPort();
        this.description = description;
    }

    @Override
    public Flux<String> getReply(String userInput) {
        return Flux.deferContextual((contextView) -> getResponse(userInput)
                        .onErrorResume(e ->
                                Flux.just(String.format((String) contextView.get(ERROR_KEY),
                                Optional.ofNullable(e.getCause())
                                        .map(Throwable::getMessage)
                                        .orElse(e.getMessage()))))
        );
    }

    @NotNull
    private Flux<String> getResponse(String userInput) {
        TerminalLogger.log("gpt input: "+userInput);
        modifyHistory(userInput);
        String body = prepareGptRequestBody();
        return doRequest(body).map(ChatGpt::resolveGptResponseStr);
    }

    private void modifyHistory(String userInput) {
        history.addLast(new HISTORY(HISTORY.USER, userInput));
        if (history.size()>4){history.removeFirst();}
    }

    @NotNull
    private String prepareGptRequestBody() {
        JSONObject body = new JSONObject();
        body.putOnce(MODEL,MODEL_VALUE);
        LinkedList<HISTORY> copyOfHistory = new LinkedList<>(history);
        copyOfHistory.addFirst(new HISTORY(HISTORY.SYSTEM,description));
        body.putOnce(MESSAGE,copyOfHistory);
        TerminalLogger.log(body.toStringPretty());
        return body.toString();
    }

    private Flux<String> doRequest(String input){
        return Flux.push(sink ->
                sink.next(HttpUtil.createPost(END_POINT)
                        .setProxy(new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(proxyHost, proxyPort)))
                        .header(Header.CONTENT_TYPE, ContentType.JSON.getValue())
                        .header(Header.AUTHORIZATION, "Bearer " + privateKey)
                        .body(input)
                        .execute()
                        .body()));
    }

    @NotNull
    private static String resolveGptResponseStr(String res) {
        TerminalLogger.log(res);
        JSONArray choices = JSONUtil.parseObj(res).getJSONArray("choices");
        JSONObject choice = choices.getJSONObject(0);
        JSONObject message = choice.getJSONObject("message");
        return message.getStr("content").trim();
    }

    @Override
    public boolean refresh() {
        history.clear();
        return true;
    }

    @AllArgsConstructor
    @Getter
    private static class HISTORY{
        static final String USER = "user";
        static final String ASSISTANT = "assistant";
        static final String SYSTEM = "system";

        private String role;

        private String content;
    }
}
