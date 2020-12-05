package name.cgt.mobtimeintellij;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.ui.Messages;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

@Service
final class TimerService {

    private final Timer timer = new Timer(true);
    private Listener listener;

    void startTimer() {
        final var showTimerExpired = new TimerTask() {
            @Override
            public void run() {
                final var app = ApplicationManager.getApplication();
                app.invokeLater(() -> {
                    Messages.showInfoMessage("The timer has expired.", "Rotate!");
                  }
                );
            }
        };
        timer.schedule(showTimerExpired, Duration.ofSeconds(5).toMillis());
        listener.onStarted();
    }

    void addListener(Listener listener) {
        this.listener = listener;
    }

    interface Listener {
        void onStarted();
    }
}
