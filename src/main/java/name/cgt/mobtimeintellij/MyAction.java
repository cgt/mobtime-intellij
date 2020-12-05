package name.cgt.mobtimeintellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class MyAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final var userClickedOK = new MyDialog().showAndGet();
        System.out.println("userClickedOK = " + userClickedOK);
    }

    @Override
    public boolean isDumbAware() {
        return false;
    }
}
