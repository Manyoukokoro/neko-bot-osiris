package org.nekotori.bot;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.utils.BotConfiguration;
import org.nekotori.config.FileBasedBotConfiguration;
import org.nekotori.event.NekoMessageEvent;
import org.nekotori.event.QQMessageEvent;
import org.nekotori.fix.FixProtocolVersion;
import org.nekotori.log.MiraiInnerLog4jLogger;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.concurrent.Executors;

public class NekoQQBot implements NekoBot<BotEvent, MessageEvent> {

    private Bot nekoBot;


    public NekoQQBot(){
        init("bot/config.yaml");
        Executors.newSingleThreadExecutor().execute(this::runBot);
    }
    public NekoQQBot(String configPath) {
        init(configPath);
        Executors.newSingleThreadExecutor().execute(this::runBot);
    }

    private void init(String path){
        var config = FileBasedBotConfiguration.resolveFile(new File(path));
        var botConfiguration = new BotConfiguration();
        botConfiguration.fileBasedDeviceInfo(config.getDeviceInfo());
        FixProtocolVersion.update();
        botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.MACOS);
        botConfiguration.setBotLoggerSupplier(b -> new MiraiInnerLog4jLogger());
        botConfiguration.setNetworkLoggerSupplier(b -> new MiraiInnerLog4jLogger());
        var account = config.getQq().getAccount();
        var passwd = config.getQq().getPasswd();
        nekoBot = BotFactory.INSTANCE.newBot(account, passwd, botConfiguration);
    }

    private void runBot(){
        nekoBot.login();
        nekoBot.join();
    }

    @Override
    public <T extends BotEvent> Flux<T> onEvent(Class<T> eventType){
        return Flux.create(fluxSink -> nekoBot.getEventChannel().subscribeAlways(
                eventType,
                fluxSink::next));
    }

    @Override
    public <T extends MessageEvent> NekoMessageEvent<T> onMessageEvent(Class<T> eventType) {
        return QQMessageEvent.of(onEvent(eventType));
    }

    @Override
    public String getId() {
        return String.valueOf(nekoBot.getId());
    }
}