package name.cgt.mobtimeintellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class StartTimer extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        new TimerService().startTimer();
    }

    @Override
    public boolean isDumbAware() {
        return false;
    }

}
