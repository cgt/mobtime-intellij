package name.cgt.mobtimeintellij;

import org.jmock.Expectations;
import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.time.Duration;

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

    private static class Timer {
        private final Display display;

        public Timer(Display display) {
            this.display = display;
        }

        public void init() {
            display.timeRemaining(Duration.ZERO);
        }
    }

    private interface Display {
        void timeRemaining(Duration time);
    }
}
