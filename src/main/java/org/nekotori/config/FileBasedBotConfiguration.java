package org.nekotori.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.setting.yaml.YamlUtil;
import lombok.Data;
import lombok.Getter;

import java.io.BufferedInputStream;
import java.io.File;

/**
 * @author nekotori
 * configuration of bot
 */

@Data
public class FileBasedBotConfiguration {

    @Getter
    public static FileBasedBotConfiguration INSTANCE = null;

    public static final String DEFAULT_CONFIG_LOCATION = "bot/config.yaml";

    static  {
        BufferedInputStream configStream = FileUtil.getInputStream(new File(DEFAULT_CONFIG_LOCATION));
        INSTANCE = YamlUtil.load(configStream, FileBasedBotConfiguration.class);
    }

    public static FileBasedBotConfiguration resolveFile(File file){
        BufferedInputStream configStream = FileUtil.getInputStream(file);
        INSTANCE = YamlUtil.load(configStream, FileBasedBotConfiguration.class);
        return INSTANCE;
    }

    private String deviceInfo;

    private QQAccount qq;

    private Discord discord;

    private Long admin;

    private String gptKey;


    @Data
    public static class QQAccount {

        private Long account;

        private String passwd;
    }

    @Data
    public static class Discord {

        private String token;

        private String proxyHost;

        private int proxyPort;
    }
}
