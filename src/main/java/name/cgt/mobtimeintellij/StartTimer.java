package name.cgt.mobtimeintellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

public class StartTimer extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        final var timer = new Timer(true);
        final var task = new TimerTask() {
            @Override
            public void run() {
                final var app = ApplicationManager.getApplication();
                app.invokeLater(() ->
                  Messages.showInfoMessage("The timer has expired.", "Rotate!")
                );
            }
        };
        timer.schedule(task, Duration.ofSeconds(5).toMillis());
    }

    @Override
    public boolean isDumbAware() {
        return false;
    }
}
