package io.darkbytes.blogapp.entity.event;

import io.darkbytes.blogapp.utit.JsonToObjectConverter;

public class EventFactory {

    public static Event convert(EventType type, String body) {
        switch (type) {
            case LIKES:
                return new JsonToObjectConverter<LikePostEvent>().convertToObject(body, LikePostEvent.class);
            case VIEW_POST:
                return new JsonToObjectConverter<ViewPostEvent>().convertToObject(body, ViewPostEvent.class);
            case NEW_COMMENT:
                return new JsonToObjectConverter<NewCommentEvent>().convertToObject(body, NewCommentEvent.class);
            case NEW_POST:
                return new JsonToObjectConverter<NewPostEvent>().convertToObject(body, NewPostEvent.class);
            case USER_CONNECTED:
                return new JsonToObjectConverter<UserConnectedEvent>().convertToObject(body, UserConnectedEvent.class);
            case DISCONNECTED:
                return new JsonToObjectConverter<UserDisconnectEvent>().convertToObject(body, UserDisconnectEvent.class);
            default:
                return new JsonToObjectConverter<Event>().convertToObject(body, Event.class);
        }
    }
}
