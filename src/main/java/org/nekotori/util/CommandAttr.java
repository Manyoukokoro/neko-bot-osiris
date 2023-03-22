package org.nekotori.util;

import lombok.Data;
import net.mamoe.mirai.message.data.SingleMessage;

import java.util.List;

/**
 * @author: JayDeng
 * @date: 03/08/2021 16:16
 * @description:
 * @version: {@link }
 */

@Data
public class CommandAttr {

    private String header;

    private String command;

    private List<String> param;

    private List<SingleMessage> extMessage;
}
    