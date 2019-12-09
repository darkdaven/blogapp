package io.darkbytes.blogapp.entity.event;

public class Event {
    private EventType type;

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Event{" +
                "type=" + type +
                '}';
    }
}
