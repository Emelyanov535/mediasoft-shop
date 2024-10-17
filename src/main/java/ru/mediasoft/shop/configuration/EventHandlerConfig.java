package ru.mediasoft.shop.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mediasoft.shop.controller.dto.EventSource;
import ru.mediasoft.shop.service.handler.EventHandler;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class EventHandlerConfig {

    @Bean
    <T extends EventSource> Set<EventHandler<T>> eventHandlers(Set<EventHandler<T>> eventHandlers) {
        return new HashSet<>(eventHandlers);
    }

//    @Bean
//    public Set<EventHandler<? extends EventSource>> eventHandlers(Set<EventHandler<? extends EventSource>> eventHandlers) {
//        return new HashSet<>(eventHandlers);
//    }
}
