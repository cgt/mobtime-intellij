package name.cgt.mobtimeintellij;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class MyAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Notifications.Bus.notify(
          new Notification(
            "myng",
            "My title",
            "My content",
            NotificationType.INFORMATION
          )
        );
    }

    @Override
    public boolean isDumbAware() {
        return false;
    }
}
