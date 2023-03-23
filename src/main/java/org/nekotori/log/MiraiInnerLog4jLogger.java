package org.nekotori.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.utils.MiraiLoggerPlatformBase;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class MiraiInnerLog4jLogger extends MiraiLoggerPlatformBase {

    public static void log(String s){
        log.info(s);
    }

    @Override
    protected void debug0(@Nullable String s, @Nullable Throwable throwable) {
        log.debug(s,throwable);
    }

    @Override
    protected void error0(@Nullable String s, @Nullable Throwable throwable) {
        log.error(s,throwable);
    }

    @Override
    protected void info0(@Nullable String s, @Nullable Throwable throwable) {
        log.info(s,throwable);
    }

    @Override
    protected void verbose0(@Nullable String s, @Nullable Throwable throwable) {
        log.info(s,throwable);
    }

    @Override
    protected void warning0(@Nullable String s, @Nullable Throwable throwable) {
        log.warn(s,throwable);
    }

    @Override
    protected void verbose0(@Nullable String message) {
        log.warn(message);
    }

    @Override
    protected void debug0(@Nullable String message) {
        log.debug(message);
    }

    @Override
    protected void error0(@Nullable String message) {
        log.error(message);
    }

    @Override
    protected void info0(@Nullable String message) {
        log.info(message);
    }

    @Override
    protected void warning0(@Nullable String message) {
        log.warn(message);
    }


    @Nullable
    @Override
    public String getIdentity() {
        return "NekoQQBot";
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