package name.cgt.mobtimeintellij;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class Mobtime {
    private final WebSocketClient ws;
    private final MyTextWebSocketHandler handler;
    final List<String> messages;
    private Consumer<String> messageListener;

    Mobtime() {
        messages = new ArrayList<>();
        final var original = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
            ws = new StandardWebSocketClient();
            handler = new MyTextWebSocketHandler(this);
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }

    public void connect(String timerName, Consumer<String> messageListener) {
        this.messageListener = messageListener;
        ws.doHandshake(handler, "ws://localhost:1234/" + timerName);
    }

    public void onConnect() {
        try {
            sendClientNew();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendClientNew() throws IOException {
        handler.session.sendMessage(new TextMessage("{\"type\": \"client:new\"}"));
    }

    public void onMessage(String message) {
        messages.add(message);
        messageListener.accept(message);
    }

    private static class MyTextWebSocketHandler extends TextWebSocketHandler {
        WebSocketSession session;

        String firstMessage;

        private final Mobtime mobtime;

        public MyTextWebSocketHandler(Mobtime mobtime) {
            this.mobtime = mobtime;
        }

        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            this.session = session;
            System.out.println("CONNECTED");
            mobtime.onConnect();
        }

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            System.out.printf("MESSAGE: %s%n", message.getPayload());
            if (firstMessage == null) {
                firstMessage = message.getPayload();
            }
            mobtime.onMessage(message.getPayload());
        }

        @Override
        public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
            exception.printStackTrace();
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
            System.out.println("DISCONNECTED");
            this.session = null;
        }

        @Override
        public boolean supportsPartialMessages() {
            return false;
        }
    }
}
