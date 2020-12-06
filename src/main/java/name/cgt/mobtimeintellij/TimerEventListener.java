package name.cgt.mobtimeintellij;

import java.time.Duration;
import java.time.Instant;

public interface TimerEventListener {
    void start(Instant now, Duration time);

    void pause(Duration time);

    void complete();
}
