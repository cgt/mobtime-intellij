package name.cgt.mobtimeintellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import org.jetbrains.annotations.NotNull;

public class MyAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final var myService = ServiceManager.getService(MyService.class);
        final var userClickedOK = new MyDialog().showAndGet();
        if (userClickedOK) {
            myService.increaseCount();
        }
        System.out.println("Count: " + myService.getCount());
    }

    @Override
    public boolean isDumbAware() {
        return false;
    }
}
