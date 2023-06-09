package org.nekotori.persistence;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class GptPersistence {

    private final static String descLoc = "bot/gpt-desc.json";

    public static void saveDescription(Long id, String desc) {
        init();
        var config = FileUtil.readUtf8String(new File(descLoc));
        Optional.ofNullable(config)
                .ifPresent(s -> {
                    JSONObject entries = JSONUtil.parseObj(s);
                    entries.replace(String.valueOf(id), desc);
                    entries.putIfAbsent(String.valueOf(id),desc);
                    FileUtil.writeString(entries.toStringPretty(), new File(descLoc), StandardCharsets.UTF_8);
                });
    }

    private static void init() {
        if(!FileUtil.exist(new File(descLoc))){
            FileUtil.writeString(new JSONObject().toStringPretty(), new File(descLoc), StandardCharsets.UTF_8);
        }
    }

    public static String getDescription(Long id) {
        init();
        var config = FileUtil.readUtf8String(new File(descLoc));
        return Optional.ofNullable(config)
                .map(s -> {
                    JSONObject entries = JSONUtil.parseObj(s);
                    return String.valueOf(entries.get(String.valueOf(id)));
                }).orElse("");
    }
}
