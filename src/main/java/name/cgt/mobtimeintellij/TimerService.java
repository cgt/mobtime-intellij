package name.cgt.mobtimeintellij;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.ui.Messages;

import java.time.Duration;
import java.time.Instant;
import java.util.TimerTask;

@Service
final class TimerService implements Display {

    private final java.util.Timer javaTimer = new java.util.Timer(true);
    private StatusTextDisplay statusText;
    private final Timer myTimer = new Timer(this);
    private final MobtimeEventTranslator eventTranslator = new MobtimeEventTranslator(myTimer);
    private final MobtimeClient mobtime = new MobtimeClient();

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

    void addStatusTextDisplay(StatusTextDisplay statusText) {
        this.statusText = statusText;
        myTimer.init();

        final var tickleMyTimer = new TimerTask() {
            @Override
            public void run() {
                myTimer.tick(Instant.now());
            }
        };
        final var initialDelay = 0;
        final var arbitrarySubsecondIntervalInMilliseconds = 250;
        javaTimer.scheduleAtFixedRate(tickleMyTimer, initialDelay, arbitrarySubsecondIntervalInMilliseconds);
    }

    interface StatusTextDisplay {
        void display(String text);
    }

    @Override
    public void timeRemaining(Duration time) {
        if (statusText == null) {
            return;
        }
        final var app = ApplicationManager.getApplication();
        app.invokeLater(() -> {
            statusText.display(String.format("%d seconds", time.toSeconds()));
        });
    }
}
