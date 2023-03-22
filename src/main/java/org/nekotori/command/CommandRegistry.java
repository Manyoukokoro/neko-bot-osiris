package org.nekotori.command;

import org.nekotori.bot.NekoBot;
import org.nekotori.log.TerminalLogger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CommandRegistry {

    public static void registry(NekoBot<?,?> bot, String className){
        try {
            Class<?> aClass = CommandRegistry.class.getClassLoader().loadClass(className);
            Method handle = aClass.getMethod("handle",NekoBot.class);
            Constructor<?> constructor = aClass.getConstructor();
            Object o = constructor.newInstance();
            handle.invoke(o, bot);
            TerminalLogger.log("registry command success! --- "+className);
        }catch (ClassNotFoundException cnfe){
            TerminalLogger.log("cannot find command: "+ className);
        }catch (NoSuchMethodException nsme){
            TerminalLogger.log("loaded class is not a command: "+ className);
        } catch (Exception e) {
            TerminalLogger.log("load command failed: "+ className);
        }
    }
}
