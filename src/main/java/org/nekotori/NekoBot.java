package org.nekotori;

import lombok.Getter;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.EventPriority;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.utils.BotConfiguration;
import org.jetbrains.annotations.NotNull;
import org.nekotori.config.FileBasedBotConfiguration;
import org.nekotori.log.TerminalLogger;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.concurrent.Executors;

public class NekoBot {

    private Bot nekoBot;

    @Getter
    Flux<GroupMessageEvent> groupMessageFlux;
    @Getter
    Flux<FriendMessageEvent> friendMessageFlux;


    public NekoBot(){
        loadConfig("config.yaml");
        groupMessageFlux = _getGroupMessageFlux();
        friendMessageFlux = _getFriendMessageFlux();
        Executors.newSingleThreadExecutor().execute(this::runBot);
    }
    public NekoBot(String configPath) {
        loadConfig(configPath);
        groupMessageFlux = _getGroupMessageFlux();
        friendMessageFlux = _getFriendMessageFlux();
        Executors.newSingleThreadExecutor().execute(this::runBot);
    }

    public void loadConfig(String path){
        FileBasedBotConfiguration config = FileBasedBotConfiguration.resolveFile(new File(path));
        BotConfiguration botConfiguration = new BotConfiguration();
        botConfiguration.fileBasedDeviceInfo(config.getDeviceInfo());
        botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PAD);
        botConfiguration.setBotLoggerSupplier(b -> new TerminalLogger());
        botConfiguration.setNetworkLoggerSupplier(b -> new TerminalLogger());
        nekoBot = BotFactory.INSTANCE.newBot(config.getBot().getQq(), config.getBot().getPasswd(), botConfiguration);
    }

    public void runBot(){
        nekoBot.login();
        nekoBot.join();
    }

    //TODO 注册ListenerHost的过程不能放到bot login之后？
    public <T extends BotEvent> Flux<T> getFlux(Class<T> eventType){
        Flux<T> flux = Flux.create(fluxSink -> nekoBot.getEventChannel().registerListenerHost(new SimpleListenerHost() {
            @NotNull
            @EventHandler(priority = EventPriority.HIGH)
            public ListeningStatus onMessage(@NotNull T event) {
                fluxSink.next(event);
                return ListeningStatus.LISTENING;
            }
        }));
        return flux;
    }

    private Flux<FriendMessageEvent> _getFriendMessageFlux(){
        return Flux.create(sink -> nekoBot.getEventChannel().registerListenerHost(new SimpleListenerHost() {
            @NotNull
            @EventHandler(priority = EventPriority.HIGH)
            public ListeningStatus onMessage(@NotNull FriendMessageEvent friendMessageEvent) {
                sink.next(friendMessageEvent);
                return ListeningStatus.LISTENING;
            }
        }));

    }


    private Flux<GroupMessageEvent> _getGroupMessageFlux(){
        return Flux.create(sink -> nekoBot.getEventChannel().registerListenerHost(new SimpleListenerHost() {
            @NotNull
            @EventHandler(priority = EventPriority.HIGH)
            public ListeningStatus onMessage(@NotNull GroupMessageEvent groupMessageEvent) {
                sink.next(groupMessageEvent);
                return ListeningStatus.LISTENING;
            }
        }));
    }
}