package org.nekotori.gpt;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.nekotori.config.FileBasedBotConfiguration;
import org.nekotori.log.TerminalLogger;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.LinkedList;

public class ChatGpt implements ChatBot {

    private static final String END_POINT = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL = "model";
    private static final String MODEL_VALUE = "gpt-3.5-turbo";
    private static final String MESSAGE = "messages";
    private LinkedList<HISTORY> history;
    private final String privateKey;

    private final String proxyHost;

    private final int proxyPort;

    public ChatGpt(){
        history = new LinkedList<>();
        privateKey = FileBasedBotConfiguration.getINSTANCE().getGptKey();
        proxyHost = FileBasedBotConfiguration.getINSTANCE().getDiscord().getProxyHost();
        proxyPort = FileBasedBotConfiguration.getINSTANCE().getDiscord().getProxyPort();
    }

    private ChatGpt(String key){
        history = new LinkedList<>();
        privateKey = key;
        proxyHost = FileBasedBotConfiguration.getINSTANCE().getDiscord().getProxyHost();
        proxyPort = FileBasedBotConfiguration.getINSTANCE().getDiscord().getProxyPort();
    }

    @Override
    public String getReply(String userInput) {
        TerminalLogger.log("gpt input: "+userInput);
        history.add(new HISTORY(HISTORY.USER,userInput));
        if (history.size()>10){
            history.removeFirst();
        }
        JSONObject body = new JSONObject();
        body.putOnce(MODEL,MODEL_VALUE);
        body.putOnce(MESSAGE,history);
        return getResponse(body);
    }

    @NotNull
    private String getResponse(JSONObject body) {
        String res = HttpUtil.createPost(END_POINT)
                .setProxy(new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(proxyHost,proxyPort)))
                .header(Header.CONTENT_TYPE, ContentType.JSON.getValue())
                .header(Header.AUTHORIZATION, "Bearer " + privateKey)
                .body(body.toString())
                .execute()
                .body();
        JSONArray choices = JSONUtil.parseObj(res).getJSONArray("choices");
        JSONObject choice = choices.getJSONObject(0);
        JSONObject message = choice.getJSONObject("message");
        return message.getStr("content").trim();
    }

    @Override
    public boolean refresh() {
        history = new LinkedList<>();
        return true;
    }

    @AllArgsConstructor
    @Getter
    private static class HISTORY{
        static final String USER = "user";

        static final String ASSISTANT = "assistant";

        private String role;

        private String content;
    }
}
