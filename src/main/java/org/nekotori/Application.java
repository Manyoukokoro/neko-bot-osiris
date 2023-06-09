package org.nekotori;

import org.nekotori.bot.NekoQQBot;
import org.nekotori.command.CommandRegistry;
import org.nekotori.command.qq.chatgpt.ChatGptCommand;
import org.nekotori.command.qq.chatgpt.ResetGptCommand;
import org.nekotori.command.qq.chatgpt.SetGptCommand;


public class Application {
    public static void main(String[] args) {
        var nekoBot = new NekoQQBot();
        CommandRegistry.registry(nekoBot, ChatGptCommand.class);
        CommandRegistry.registry(nekoBot, ResetGptCommand.class);
        CommandRegistry.registry(nekoBot, SetGptCommand.class);
    }
}
