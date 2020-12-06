package name.cgt.mobtimeintellij;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.ParseException;

import java.time.Duration;
import java.time.Instant;

class MobtimeEventTranslator {
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
        } else if ("timer:pause".equals(type)) {
            final var timerDuration = e.getLong("timerDuration", -1);
            if (timerDuration >= 0) {
                listener.pause(Duration.ofMillis(timerDuration));
            }
        }
    }
}
