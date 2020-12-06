package name.cgt.mobtimeintellij;

import com.eclipsesource.json.Json;
import org.jmock.Expectations;
import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

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
    public void timer_complete() {
        context.checking(new Expectations() {{
            oneOf(listener).complete();
        }});
        translator.onEvent("{\"type\":\"timer:complete\"}");
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
            final var e = Json.parse(event).asObject();
            final var type = e.getString("type", null);
            if ("timer:complete".equals(type)) {
                listener.complete();
            }
        }
    }
}
