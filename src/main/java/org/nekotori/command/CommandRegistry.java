package org.nekotori.command;

import lombok.extern.slf4j.Slf4j;
import org.nekotori.bot.NekoBot;

@Slf4j
public class CommandRegistry {

    public static void registry(NekoBot<?,?> bot, String className){
        try {
            var aClass = CommandRegistry.class.getClassLoader().loadClass(className);
            var handle = aClass.getMethod("handle",NekoBot.class);
            var constructor = aClass.getConstructor();
            var o = constructor.newInstance();
            handle.invoke(o, bot);
            log.info("registry command success! --- {}", className);
        }catch (ClassNotFoundException cnfe){
            log.error("cannot find command: {}", className);
        }catch (NoSuchMethodException nsme){
            log.error("loaded class is not a command: {}", className);
        } catch (Exception e) {
            log.error("load command failed: {}", className);
        }
    }

    public static void registry(NekoBot<?,?> bot, Class<?> clazz){
        try{
            var handle = clazz.getMethod("handle",NekoBot.class);
            var constructor = clazz.getConstructor();
            var o = constructor.newInstance();
            handle.invoke(o, bot);
            log.info("registry command success! --- {}", clazz.getName());
        }catch (NoSuchMethodException nsme){
            log.error("loaded class is not a command: {}", clazz.getName());
        } catch (Exception e) {
            log.error("load command failed: {}", clazz.getName());
        }
    }
}
