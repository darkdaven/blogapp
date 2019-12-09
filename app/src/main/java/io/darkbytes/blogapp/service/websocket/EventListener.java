package io.darkbytes.blogapp.service.websocket;

import io.darkbytes.blogapp.entity.event.Event;
import io.darkbytes.blogapp.entity.event.EventFactory;
import io.darkbytes.blogapp.entity.event.LikePostEvent;
import io.darkbytes.blogapp.entity.event.NewCommentEvent;
import io.darkbytes.blogapp.entity.event.NewPostEvent;
import io.darkbytes.blogapp.entity.event.UserConnectedEvent;
import io.darkbytes.blogapp.entity.event.UserDisconnectEvent;
import io.darkbytes.blogapp.entity.event.ViewPostEvent;

public abstract class EventListener implements Listener {

    public void consume(Event event, String body) {
        switch (event.getType()) {
            case VIEW_POST:
                this.onViewPostEvent((ViewPostEvent) EventFactory.convert(event.getType(), body));
                break;
            case LIKES:
                this.onLikePostEvent((LikePostEvent) EventFactory.convert(event.getType(), body));
                break;
            case NEW_COMMENT:
                this.onNewCommentEvent((NewCommentEvent) EventFactory.convert(event.getType(), body));
                break;
            case NEW_POST:
                this.onNewPostEvent((NewPostEvent) EventFactory.convert(event.getType(), body));
                break;
            case USER_CONNECTED:
                this.onUserConnected((UserConnectedEvent) EventFactory.convert(event.getType(), body));
                break;
            case DISCONNECTED:
                this.onUserDisconnect((UserDisconnectEvent) EventFactory.convert(event.getType(), body));
                break;
        }
    }

    public abstract void onLikePostEvent(LikePostEvent event);

    public abstract void onViewPostEvent(ViewPostEvent event);

    public abstract void onNewCommentEvent(NewCommentEvent event);

    public abstract void onNewPostEvent(NewPostEvent event);

    public abstract void onUserConnected(UserConnectedEvent event);

    public abstract void onUserDisconnect(UserDisconnectEvent event);
}
