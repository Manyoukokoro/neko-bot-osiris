package org.nekotori.event;

public abstract class NekoCommandEvent<E> extends NekoMessageEvent<E> {

    @Override
    public NekoMessageEvent<E> onVerify(Class<?> clazz) {
        return null;
    }
}
