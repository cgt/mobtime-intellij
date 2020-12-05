package name.cgt.mobtimeintellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import org.jetbrains.annotations.NotNull;

public class StartTimer extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        ServiceManager.getService(TimerService.class).startTimer();
    }

    @Override
    public boolean isDumbAware() {
        return false;
    }

}
