package name.cgt.mobtimeintellij;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.Messages;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

class TimerService {
    void startTimer() {
        final var timer = new Timer(true);
        final var showTimerExpired = new TimerTask() {
            @Override
            public void run() {
                final var app = ApplicationManager.getApplication();
                app.invokeLater(() ->
                  Messages.showInfoMessage("The timer has expired.", "Rotate!")
                );
            }
        };
        timer.schedule(showTimerExpired, Duration.ofSeconds(5).toMillis());
    }
}
