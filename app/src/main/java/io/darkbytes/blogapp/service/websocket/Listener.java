package io.darkbytes.blogapp.service.websocket;

import io.darkbytes.blogapp.entity.event.Event;

public interface Listener {
    void consume(Event event, String body);
}
