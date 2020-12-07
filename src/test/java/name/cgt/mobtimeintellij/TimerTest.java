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

    private final MobtimerListener display = context.mock(MobtimerListener.class);
    private final Mobtimer timer = new Mobtimer(display);

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
            oneOf(display).timeRemaining(seconds(60));
        }});

        timer.start(Instant.now(), seconds(60));
    }

    @Test
    public void on_tick_update_and_display_time_remaining() {
        final var t1 = Instant.now();
        final var t2 = t1.plusSeconds(1);

        context.checking(new Expectations() {{
            final var update = context.sequence("update");

            oneOf(display).timeRemaining(seconds(60));
            inSequence(update);

            oneOf(display).timeRemaining(seconds(59));
            inSequence(update);
        }});

        timer.start(t1, seconds(60));
        timer.tick(t2);
    }

    @Test
    public void stop_updating_display_while_paused() {
        final var t2 = Instant.now().plusSeconds(2);

        context.checking(new Expectations() {{
            oneOf(display).timeRemaining(seconds(41));
        }});

        timer.pause(seconds(41));
        timer.tick(t2);
    }

    @Test
    public void when_completed_display_no_time_remaining() {
        context.checking(new Expectations() {{
            oneOf(display).timeRemaining(Duration.ZERO);
        }});

        timer.complete();
    }

    @Test
    public void stop_updating_display_after_completion() {
        final var t1 = Instant.now();
        final var t2 = t1.plusSeconds(1);

        context.checking(new Expectations() {{
            final var update = context.sequence("update");

            oneOf(display).timeRemaining(seconds(60));
            inSequence(update);

            oneOf(display).timeRemaining(Duration.ZERO);
            inSequence(update);
        }});

        timer.start(t1, seconds(60));
        timer.complete();
        timer.tick(t2);
    }

    @Test
    public void is_completed_when_no_time_remaining() {
        final var t1 = Instant.now();
        final var t2 = t1.plusSeconds(1);
        final var t3 = t2.plusSeconds(1);

        context.checking(new Expectations() {{
            oneOf(display).timeRemaining(seconds(1));
            oneOf(display).timeRemaining(Duration.ZERO);
        }});

        timer.start(t1, seconds(1));
        timer.tick(t2);
        timer.tick(t3);
    }

    @Test
    public void example_of_pausing_and_resuming() {
        final var t2 = Instant.now();
        final var t3 = t2.plusSeconds(1);

        context.checking(new Expectations() {{
            exactly(2).of(display).timeRemaining(seconds(10));
        }});

        timer.pause(seconds(10));
        timer.tick(t2);
        timer.start(t3, seconds(10));
    }

    private static Duration seconds(int n) {
        return Duration.ofSeconds(n);
    }

}
