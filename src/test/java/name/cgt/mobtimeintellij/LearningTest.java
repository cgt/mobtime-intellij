package name.cgt.mobtimeintellij;

import org.junit.jupiter.api.Test;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LearningTest {
    @Test
    public void applesauce() {

        final var mobtime = new Mobtime();
        mobtime.connect("wghweugwgoweg");
        await()
          .atMost(Duration.ofSeconds(5))
          .untilAsserted(() ->
            assertEquals(List.of("{\"type\":\"timer:ownership\",\"isOwner\":true}"), mobtime.messages)
          );
    }

    private static class Mobtime {
        private final WebSocketClient ws = new StandardWebSocketClient();
        private final MyTextWebSocketHandler handler = new MyTextWebSocketHandler(this);
        final List<String> messages = new ArrayList<>();

        public void connect(String timerName) {
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
        }
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
