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

class MobtimeClient {
    private WebSocketClient ws;
    private MyTextWebSocketHandler handler;
    final List<String> messages;
    private Consumer<String> messageListener;

    MobtimeClient() {
        messages = new ArrayList<>();
        initializeWebSocketClient();
    }

    private void initializeWebSocketClient() {
        final var original = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
            ws = new StandardWebSocketClient();
            handler = new MyTextWebSocketHandler(this);
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }

    public void connect(String timerURL, Consumer<String> messageListener) {
        this.messageListener = messageListener;
        ws.doHandshake(handler, timerURL);
    }

    public boolean isConnected() {
        final var session = handler.session;
        return session != null && session.isOpen();
    }

    public void disconnect() {
        final var session = handler.session;
        if (session != null) {
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            initializeWebSocketClient();
        }
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

        private final MobtimeClient mobtime;

        public MyTextWebSocketHandler(MobtimeClient mobtime) {
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
