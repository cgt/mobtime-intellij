package name.cgt.mobtimeintellij;

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

    @Test
    public void timer_pause() {
        context.checking(new Expectations() {{
            oneOf(listener).pause(Duration.ofMillis(1234));
        }});
        translator.onEvent("{\"type\":\"timer:pause\",\"timerDuration\":1234}");
    }

}
