package name.cgt.mobtimeintellij;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.TimerTask;

@Service
final class TimerService implements Display {

    private final java.util.Timer timer = new java.util.Timer(true);
    private StatusText statusText;
    private final Timer myTimer = new Timer(this);

    void startTimer() {
        myTimer.start(Instant.now(), Duration.ofSeconds(10));
    }

    void addListener(StatusText statusText) {
        this.statusText = statusText;
        myTimer.init();

        final var tickleMyTimer = new TimerTask() {
            @Override
            public void run() {
                myTimer.tick(Instant.now());
            }
        };
        timer.scheduleAtFixedRate(tickleMyTimer, 0, 250);
    }

    interface StatusText {
        void setLabelText(String s);
    }

    @Override
    public void timeRemaining(Duration time) {
        if (statusText == null) {
            return;
        }
        final var app = ApplicationManager.getApplication();
        app.invokeLater(() -> {
            statusText.setLabelText(String.format("%d seconds", time.toSeconds()));
        });
    }
}
