package name.cgt.mobtimeintellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class StartTimer extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Messages.showInfoMessage("The timer has expired.", "Rotate!");
    }

    @Override
    public boolean isDumbAware() {
        return false;
    }
}
