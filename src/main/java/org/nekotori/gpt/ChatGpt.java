package org.nekotori.gpt;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.netty.handler.codec.http.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.nekotori.config.FileBasedBotConfiguration;
import org.nekotori.resource.ReactorResources;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
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
        return Flux.deferContextual((contextView) -> getResponse(userInput).onErrorResume(e ->
                                Flux.just(String.format((String) contextView.get(ERROR_KEY),
                                Optional.ofNullable(e.getCause())
                                        .map(Throwable::getMessage)
                                        .orElse(e.getMessage()))))
        );
    }

    @NotNull
    private Flux<String> getResponse(String userInput) {
        log.info("gpt input: {}", userInput);
        modifyHistory(userInput);
        var body = prepareGptRequestBody();
        return doRequest(body).map(ChatGpt::resolveGptResponseStr);
    }

    private void modifyHistory(String userInput) {
        history.addLast(new HISTORY(HISTORY.USER, userInput));
        if (history.size()>4){history.removeFirst();}
    }

    @NotNull
    private String prepareGptRequestBody() {
        var body = new JSONObject();
        body.putOnce(MODEL,MODEL_VALUE);
        var copyOfHistory = new LinkedList<>(history);
        copyOfHistory.addFirst(new HISTORY(HISTORY.SYSTEM,description));
        body.putOnce(MESSAGE,copyOfHistory);
        log.info(body.toStringPretty());
        return body.toString();
    }

    private Flux<String> doRequest(String input){
        var client = ReactorResources.DEFAULT_HTTP_CLIENT;
        return client.get().proxy(spec->spec.type(ProxyProvider.Proxy.HTTP).address(InetSocketAddress.createUnresolved(proxyHost,proxyPort)))
                            .baseUrl(END_POINT)
                            .secure()
                            .headersWhen(headers-> Mono.create(headerSink -> {
                                headers.add(Header.CONTENT_TYPE.name(), ContentType.JSON.getValue());
                                headers.add(Header.AUTHORIZATION.name(),"Bearer " + privateKey);
                                headerSink.success(headers);
                            }))
                            .request(HttpMethod.POST)
                            .send(ByteBufFlux.fromString(Flux.just(input)))
                            .response( (resp,flux) ->  flux.asString(StandardCharsets.UTF_8));
    }

    @NotNull
    private static String resolveGptResponseStr(String res) {
        log.info(res);
        return JSONUtil.parseObj(res)
                .getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getStr("content")
                .trim();
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
