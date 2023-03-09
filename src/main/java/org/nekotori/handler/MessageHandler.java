package org.nekotori.handler;

public interface MessageHandler<T> {
    void handle(T message);
}
