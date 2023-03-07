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

    public static FileBasedBotConfiguration resolveFile(File file){
        BufferedInputStream configStream = FileUtil.getInputStream(file);
        INSTANCE = YamlUtil.load(configStream, FileBasedBotConfiguration.class);
        return INSTANCE;
    }

    private String deviceInfo;

    private Account bot;

    private Long admin;

    private String gptKey;


    @Data
    public static class Account{
        private Long qq;

        private String passwd;
    }
}
