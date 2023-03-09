package org.nekotori.bot;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.utils.BotConfiguration;
import org.nekotori.config.FileBasedBotConfiguration;
import org.nekotori.log.TerminalLogger;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.concurrent.Executors;

public class NekoQQBot implements NekoBot<BotEvent> {

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
        FileBasedBotConfiguration config = FileBasedBotConfiguration.resolveFile(new File(path));
        BotConfiguration botConfiguration = new BotConfiguration();
        botConfiguration.fileBasedDeviceInfo(config.getDeviceInfo());
        botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PAD);
        botConfiguration.setBotLoggerSupplier(b -> new TerminalLogger());
        botConfiguration.setNetworkLoggerSupplier(b -> new TerminalLogger());
        Long account = config.getQq().getAccount();
        String passwd = config.getQq().getPasswd();
        nekoBot = BotFactory.INSTANCE.newBot(account, passwd, botConfiguration);
    }

    private void runBot(){
        nekoBot.login();
        nekoBot.join();
    }

    @Override
    public <T extends BotEvent> Flux<T> listenOn(Class<T> eventType){
        return Flux.create(fluxSink -> nekoBot.getEventChannel().subscribeAlways(
                eventType,
                fluxSink::next));
    }
}