package org.nekotori.bot;

import discord4j.common.ReactorResources;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.gateway.GatewayReactorResources;
import io.netty.util.internal.StringUtil;
import org.nekotori.config.FileBasedBotConfiguration;
import org.nekotori.config.FileBasedBotConfiguration.Discord;
import org.nekotori.event.DCMessageEvent;
import org.nekotori.event.NekoMessageEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

import java.io.File;
import java.util.Optional;

public class NekoDiscordBot implements NekoBot<Event, MessageCreateEvent> {


    private Mono<GatewayDiscordClient> gateway;

    public NekoDiscordBot(){
        init("bot/config.yaml");
    }


    private void init(String path){
        //get proxy configuration from config yaml file.
        var config = FileBasedBotConfiguration.resolveFile(new File(path));
        var discord = Optional.ofNullable(config)
                .map(FileBasedBotConfiguration::getDiscord)
                .orElse(new Discord());
        var hostExistsOption = Optional.of(discord)
                .map(Discord::getProxyHost)
                .flatMap(host -> StringUtil.isNullOrEmpty(host) ? Optional.empty() : Optional.of(host));

        //config proxy if it's defined in configuration file.
        var secure  = hostExistsOption.map(host-> HttpClient.create()
                    .proxy(addressSpec -> addressSpec
                            .type(ProxyProvider.Proxy.SOCKS5)
                            .host(discord.getProxyHost())
                            .port(discord.getProxyPort()))
                .compress(true)).orElseGet(HttpClient::create);

        var proxyHttpClient =ReactorResources.builder()
                .httpClient(secure)
                .build();

        //the proxy http client should be configured twice in client builder and gateway builder.
        var dcClient = hostExistsOption.map(host-> DiscordClient.builder(discord.getToken())
                .setReactorResources(proxyHttpClient)
                .build()).orElseGet(()->DiscordClient.create(discord.getToken()));
        gateway = hostExistsOption.map(host->dcClient.gateway()
                .setGatewayReactorResources(resources -> new GatewayReactorResources(proxyHttpClient))
                .login()).orElseGet(()->dcClient.gateway().login());
    }

    @Override
    public <T extends Event> Flux<T> onEvent(Class<T> eventType) {
        return gateway.flux().concatMap(gw -> gw.on(eventType));
    }

    //TODO finish discord message handler.
    @Override
    public <T extends MessageCreateEvent> NekoMessageEvent<T> onMessageEvent(Class<T> eventType) {
        Flux<T> tFlux = gateway.flatMapMany(client -> client.on(eventType));
        return DCMessageEvent.of(tFlux);
    }

    //TODO use discord guild Id?
    @Override
    public String getId() {
        return null;
    }
}
