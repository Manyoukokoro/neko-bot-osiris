package org.nekotori.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.mamoe.mirai.utils.MiraiLoggerPlatformBase;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TerminalLogger extends MiraiLoggerPlatformBase {

    @Override
    protected void debug0(@Nullable String s, @Nullable Throwable throwable) {
        System.out.println(new Log(Level.DEBUG, s, LocalDateTime.now()));
    }

    @Override
    protected void error0(@Nullable String s, @Nullable Throwable throwable) {
        System.out.println(new Log(Level.ERROR, s, LocalDateTime.now()));
    }

    @Override
    protected void info0(@Nullable String s, @Nullable Throwable throwable) {
        System.out.println(new Log(Level.INFO, s, LocalDateTime.now()));
    }

    @Override
    protected void verbose0(@Nullable String s, @Nullable Throwable throwable) {
        System.out.println(new Log(Level.INFO, s, LocalDateTime.now()));
    }

    @Override
    protected void warning0(@Nullable String s, @Nullable Throwable throwable) {
        System.out.println(new Log(Level.WARN, s, LocalDateTime.now()));
    }

    @Override
    protected void verbose0(@Nullable String message) {
        System.out.println(new Log(Level.INFO, message, LocalDateTime.now()));
    }

    @Override
    protected void debug0(@Nullable String message) {
        System.out.println(new Log(Level.DEBUG, message, LocalDateTime.now()));
    }

    @Override
    protected void error0(@Nullable String message) {
        System.out.println(new Log(Level.ERROR, message, LocalDateTime.now()));
    }

    @Override
    protected void info0(@Nullable String message) {
        System.out.println(new Log(Level.INFO, message, LocalDateTime.now()));
    }

    @Override
    protected void warning0(@Nullable String message) {
        System.out.println(new Log(Level.WARN, message, LocalDateTime.now()));
    }


    @Nullable
    @Override
    public String getIdentity() {
        return "NekoBot";
    }

    @Data
    @AllArgsConstructor
    static class Log{
        private Level level;
        private String info;
        private LocalDateTime time;

        public String toString(){
            return time.format(DateTimeFormatter.BASIC_ISO_DATE)+">>"
                    +level.name()+">>"
                    +info;
        }
    }

    static enum Level{
        INFO,ERROR,WARN,DEBUG;
    }
}