package name.cgt.mobtimeintellij;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.ui.Messages;

import java.time.Duration;
import java.time.Instant;
import java.util.TimerTask;

@Service
final class TimerService implements Display {

    private final java.util.Timer timer = new java.util.Timer(true);
    private StatusText statusText;
    private final Timer myTimer = new Timer(this);
    private final MobtimeEventTranslator eventTranslator = new MobtimeEventTranslator(myTimer);
    private final Mobtime mobtime = new Mobtime();

    void startTimer() {
        if (mobtime.isConnected()) {
            ApplicationManager.getApplication().executeOnPooledThread(() -> {
                myTimer.complete();
                mobtime.disconnect();
            });
            return;
        }

        final var timerName = Messages.showInputDialog("Enter name of your Mobtimer", "Mobtime", null);
        if (timerName == null || timerName.isBlank()) {
            System.err.println("User entered blank timer name.");
            return;
        }
        System.out.println("Connecting to timer " + timerName);
        ApplicationManager.getApplication().executeOnPooledThread(() ->
          mobtime.connect(timerName, eventTranslator::onEvent)
        );
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
        void set(String s);
    }

    @Override
    public void timeRemaining(Duration time) {
        if (statusText == null) {
            return;
        }
        final var app = ApplicationManager.getApplication();
        app.invokeLater(() -> {
            statusText.set(String.format("%d seconds", time.toSeconds()));
        });
    }
}
