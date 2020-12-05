package name.cgt.mobtimeintellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class MyAction extends AnAction {
    private boolean wasExecuted = false;

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        System.out.println("Execute MyAction again!!");
        Messages.showInfoMessage(
          "This action will be disabled after first use.",
          "An Excellent Dialog"
        );
        wasExecuted = true;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(!wasExecuted);
    }

    @Override
    public boolean isDumbAware() {
        return false;
    }
}
