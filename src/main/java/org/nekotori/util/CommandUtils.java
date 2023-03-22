package org.nekotori.util;

import io.netty.util.internal.StringUtil;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.SingleMessage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: JayDeng
 * @date: 03/08/2021 16:15
 * @description:
 * @version: {@link }
 */
public class CommandUtils {

    public static final String[] commandHeader = new String[]{"!","#"};

    public static CommandAttr resolveTextCommand(String message) {
        CommandAttr commandAttr = new CommandAttr();
        commandAttr.setHeader(Arrays.stream(commandHeader).filter(message::startsWith).findFirst().orElse(""));
        message = message.replaceFirst(commandAttr.getHeader(), "");
        final String[] s = message.split("[ \n]");
        List<String> collect = Arrays.stream(s).filter(single -> !single.equals("")).collect(Collectors.toList());
        if (collect.size() > 0) commandAttr.setCommand(collect.get(0));
        if (collect.size() > 1) {
            List<String> strings = collect.subList(1, collect.size());
            commandAttr.setParam(strings);
        }

        return commandAttr;
    }

    public static CommandAttr resolveCommand(MessageChain message,Group subject) {
        int size = message.size();
        if (size < 3) {
            return resolveTextCommand(CommandUtils.transMessageToText(message,subject));
        }
        List<SingleMessage> singleMessages = message.subList(2, size);
        MessageChain build = new MessageChainBuilder().append(message.get(1)).build();
        CommandAttr commandAttr = resolveTextCommand(CommandUtils.transMessageToText(build,subject));
        commandAttr.setExtMessage(singleMessages);
        return commandAttr;
    }

    public static boolean isCommand(GroupMessageEvent groupMessageEvent) {
        String s = transMessageEventToText(groupMessageEvent);
        for (String c : commandHeader) {
            if (s.startsWith(c)) return true;
        }
        return false;
    }
    public static List<String> resolveRegisteredCommand(String command) {
        if (StringUtil.isNullOrEmpty(command)) return new ArrayList<>();
        String[] commands = command.trim().split("#");
        List<String> strings = Arrays.asList(commands);
        return new ArrayList<>(strings);
    }

    public static boolean IsCommandRegistered(String registeredCommand, String command) {
        List<String> commands = resolveRegisteredCommand(registeredCommand);
        for (String c : commands) {
            if (c.equals(command)) {
                return true;
            }
        }
        return false;
    }

    public static String addCommand(String registeredCommand, String command) {
        if (IsCommandRegistered(registeredCommand, command)) return registeredCommand;
        List<String> commands = resolveRegisteredCommand(registeredCommand);
        commands.add(command);
        return String.join("#", commands);
    }

    public static String removeCommand(String registeredCommand, String command) {
        if (!IsCommandRegistered(registeredCommand, command)) return registeredCommand;
        List<String> commands = resolveRegisteredCommand(registeredCommand);
        commands.remove(command);
        return String.join("#", commands);
    }

    public static String replaceCommand(String registeredCommand, String oldCommand, String newCommand) {
        if (!IsCommandRegistered(registeredCommand, oldCommand)) return registeredCommand;
        List<String> commands = resolveRegisteredCommand(registeredCommand);
        commands.remove(oldCommand);
        commands.add(newCommand);
        return String.join("#", commands);
    }

    @NotNull
    public static String transMessageEventToText(GroupMessageEvent groupMessageEvent) {
        return transMessageToText(groupMessageEvent.getMessage(),groupMessageEvent.getSubject());
    }

    @NotNull
    public static String transMessageToText(MessageChain messages, Group subject) {
        return  messages.serializeToMiraiCode();
    }
}
    