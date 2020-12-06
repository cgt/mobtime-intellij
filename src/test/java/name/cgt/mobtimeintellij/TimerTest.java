package name.cgt.mobtimeintellij;

import org.jmock.Expectations;
import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.time.Duration;
import java.time.Instant;

public class TimerTest {
    @RegisterExtension
    final JUnit5Mockery context = new JUnit5Mockery();

    private final Display display = context.mock(Display.class);
    private final Timer timer = new Timer(display);

    @Test
    public void when_initialized_display_no_time_remaining() {
        context.checking(new Expectations() {{
            oneOf(display).timeRemaining(Duration.ZERO);
        }});

        timer.init();
    }

    @Test
    public void when_started_display_time_remaining() {
        context.checking(new Expectations() {{
            oneOf(display).timeRemaining(Duration.ofSeconds(60));
        }});

        timer.start(Instant.now(), Duration.ofSeconds(60));
    }

    @Test
    public void on_tick_update_and_display_time_remaining() {
        final var t1 = Instant.now();
        final var t2 = t1.plusSeconds(1);

        context.checking(new Expectations() {{
            final var update = context.sequence("update");

            oneOf(display).timeRemaining(Duration.ofSeconds(60));
            inSequence(update);

            oneOf(display).timeRemaining(Duration.ofSeconds(59));
            inSequence(update);
        }});

        timer.start(t1, Duration.ofSeconds(60));
        timer.tick(t2);
    }

    private static class Timer {
        private final Display display;
        private Instant startTime;
        private Duration duration;

        public Timer(Display display) {
            this.display = display;
        }

        public void init() {
            display.timeRemaining(Duration.ZERO);
        }

        public void start(Instant now, Duration time) {
            startTime = now;
            duration = time;
            display.timeRemaining(time);
        }

        public void tick(Instant now) {
            final var elapsed = Duration.between(startTime, now);
            final var remaining = duration.minus(elapsed);
            display.timeRemaining(remaining);
        }
    }

    private interface Display {
        void timeRemaining(Duration time);
    }
}
