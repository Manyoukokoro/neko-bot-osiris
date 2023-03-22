package org.nekotori;

import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import org.nekotori.bot.NekoBot;
import org.nekotori.bot.NekoQQBot;
import org.nekotori.command.CommandRegistry;


public class Application {
    public static void main(String[] args) {
        NekoBot<BotEvent, MessageEvent> nekoBot = new NekoQQBot();

        CommandRegistry.registry(nekoBot,"org.nekotori.command.qq.SetGptCommand");
        CommandRegistry.registry(nekoBot,"org.nekotori.command.qq.ReSetGptCommand");
        CommandRegistry.registry(nekoBot,"org.nekotori.command.qq.ChatGptCommand");

        System.gc();

    }
}
