package name.cgt.mobtimeintellij;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;

class Timer {
    private final Display display;
    @Nullable
    private Instant startTime;
    @NotNull
    private Duration duration = Duration.ZERO;

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

    public void pause(Duration time) {
        startTime = null;
        duration = time;
        display.timeRemaining(time);
    }

    public void complete() {
        startTime = null;
        display.timeRemaining(Duration.ZERO);
    }

    public void tick(Instant now) {
        if (startTime == null) {
            return;
        }
        final var elapsed = Duration.between(startTime, now);
        if (isLessThanOneSecond(elapsed)) {
            return;
        }
        final var remaining = duration.minus(elapsed);
        if (isLessThanOneSecond(remaining)) {
            complete();
        } else {
            display.timeRemaining(remaining);
        }
    }

    private boolean isLessThanOneSecond(Duration d) {
        return d.compareTo(Duration.ofSeconds(1)) < 0;
    }
}
