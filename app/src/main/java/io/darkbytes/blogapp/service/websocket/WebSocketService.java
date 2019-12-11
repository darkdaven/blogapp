package io.darkbytes.blogapp.service.websocket;

import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import io.darkbytes.blogapp.Constant;
import io.darkbytes.blogapp.entity.event.Event;
import io.darkbytes.blogapp.util.JsonToObjectConverter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketService {

    private static WebSocketService instance = null;
    private static BlogWebSocketListener wsListener;

    private WebSocketService(String token) {
        Request request = new Request.Builder()
                .url(String.format("%s?token=%s", Constant.WS_PATH, token))
                .build();

        OkHttpClient client = new OkHttpClient();
        wsListener = new BlogWebSocketListener();
        client.newWebSocket(request, wsListener);
    }

    public static WebSocketService getInstance(String token) {
        if (instance == null) {
            instance = new WebSocketService(token);
        }

        return instance;
    }

    public void subscribe(Listener listener) {
        wsListener.subscribe(listener);
    }

    private final class BlogWebSocketListener extends WebSocketListener {
        private final Set<Listener> listeners = new CopyOnWriteArraySet<>();

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);

            this.listeners.stream().forEach(l -> {
                Event e = new JsonToObjectConverter<Event>().convertToObject(text, Event.class);
                l.consume(e, text);
            });
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            Log.e("BlogWebSocketListener onClosed", reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
            super.onFailure(webSocket, t, response);
            try {
                Log.e("BlogWebSocketListener", (response != null) ? response.body().string() : "");
                Log.e("BlogWebSocketListener onFailure", t.getMessage());
                t.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            try {
                Log.e("BlogWebSocketListener onOpen", response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void subscribe(Listener listener) {
            this.listeners.add(listener);
        }
    }
}
