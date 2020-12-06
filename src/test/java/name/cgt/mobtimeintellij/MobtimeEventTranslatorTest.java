package name.cgt.mobtimeintellij;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.ParseException;
import org.jmock.Expectations;
import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.time.Duration;
import java.time.Instant;

public class MobtimeEventTranslatorTest {
    @RegisterExtension
    final JUnit5Mockery context = new JUnit5Mockery();

    private final TimerEventListener listener = context.mock(TimerEventListener.class);
    private final MobtimeEventTranslator translator = new MobtimeEventTranslator(listener);

    @Test
    public void discard_empty_event() {
        context.checking(new Expectations() {{
            never(listener);
        }});
        translator.onEvent(null);
        translator.onEvent("");
    }

    @Test
    public void discard_event_with_invalid_JSON() {
        context.checking(new Expectations() {{
            never(listener);
        }});
        translator.onEvent("<this is definitely not json>");
    }

    @Test
    public void timer_complete() {
        context.checking(new Expectations() {{
            oneOf(listener).complete();
        }});
        translator.onEvent("{\"type\":\"timer:complete\"}");
    }

    @Test
    public void timer_start() {
        context.checking(new Expectations() {{
            oneOf(listener).start(with(aNonNull(Instant.class)), with(Duration.ofMillis(300000)));
        }});
        translator.onEvent("{\"type\":\"timer:start\",\"timerDuration\":300000}");
    }

    private static class MobtimeEventTranslator {
        private final TimerEventListener listener;

        public MobtimeEventTranslator(TimerEventListener listener) {
            this.listener = listener;
        }

        public void onEvent(String event) {
            if (event == null || event.isBlank()) {
                return;
            }
            final JsonObject e;
            try {
                e = Json.parse(event).asObject();
            } catch (ParseException ex) {
                return;
            }
            final var type = e.getString("type", null);
            if ("timer:complete".equals(type)) {
                listener.complete();
            } else if ("timer:start".equals(type)) {
                final var timerDuration = e.getLong("timerDuration", -1);
                if (timerDuration >= 0) {
                    listener.start(Instant.now(), Duration.ofMillis(timerDuration));
                }
            }
        }
    }
}
