package name.cgt.mobtimeintellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import org.jetbrains.annotations.NotNull;

public class StartTimer extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        final var timer = ServiceManager.getService(TimerService.class);
        timer.startTimer();
    }

    @Override
    public boolean isDumbAware() {
        return false;
    }

}
