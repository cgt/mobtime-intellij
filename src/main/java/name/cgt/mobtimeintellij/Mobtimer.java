package name.cgt.mobtimeintellij;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;

class Mobtimer implements TimerEventListener {
    private final MobtimerListener listener;
    @Nullable
    private Instant startTime;
    @NotNull
    private Duration duration = Duration.ZERO;

    public Mobtimer(MobtimerListener listener) {
        this.listener = listener;
    }

    public void init() {
        listener.timeRemaining(Duration.ZERO);
    }

    @Override
    public void start(Instant now, Duration time) {
        startTime = now;
        duration = time;
        listener.timeRemaining(time);
    }

    @Override
    public void pause(Duration time) {
        startTime = null;
        duration = time;
        listener.timeRemaining(time);
    }

    @Override
    public void complete() {
        startTime = null;
        listener.timeRemaining(Duration.ZERO);
    }

    public void tick(Instant now) {
        if (startTime == null) {
            return;
        }
        final var elapsed = Duration.between(startTime, now);
        final var remaining = duration.minus(elapsed);
        if (isLessThanOneSecond(remaining)) {
            complete();
        } else {
            listener.timeRemaining(remaining);
        }
    }

    private boolean isLessThanOneSecond(Duration d) {
        return d.compareTo(Duration.ofSeconds(1)) < 0;
    }
}
