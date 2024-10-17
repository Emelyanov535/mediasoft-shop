package ru.mediasoft.shop.service.handler;

import ru.mediasoft.shop.controller.dto.EventSource;

public interface EventHandler <T extends EventSource> {

    boolean canHandle(EventSource eventSource);

    void handleEvent(T eventSource);
}
