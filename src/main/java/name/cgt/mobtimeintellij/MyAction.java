package name.cgt.mobtimeintellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class MyAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        System.out.println("Execute MyAction again!!");
        Messages.showInfoMessage(
          "Message from MyAction: " + e.getPresentation().getDescription(),
          "An Excellent Dialog"
        );
    }

    @Override
    public boolean isDumbAware() {
        return false;
    }
}
