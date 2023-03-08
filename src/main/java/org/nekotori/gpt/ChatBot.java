package org.nekotori.gpt;

public interface ChatBot {
    String getReply(String userInput);

    boolean refresh();

}