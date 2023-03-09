package org.nekotori.gpt;

import reactor.core.publisher.Flux;

public interface ChatBot {
    String ERROR_KEY = "errorMessage";
    Flux<String> getReply(String userInput);

    boolean refresh();

}