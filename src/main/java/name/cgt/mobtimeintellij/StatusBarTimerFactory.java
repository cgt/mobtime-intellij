package name.cgt.mobtimeintellij;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.StatusBarWidgetFactory;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class StatusBarTimerFactory implements StatusBarWidgetFactory {
    @Override
    @NonNls
    @NotNull
    public String getId() {
        return StatusLabel.id;
    }

    @Override
    @Nls
    @NotNull
    public String getDisplayName() {
        return "Mobtime countdown";
    }

    @Override
    public boolean isAvailable(@NotNull Project project) {
        return true;
    }

    @Override
    @NotNull
    public StatusBarWidget createWidget(@NotNull Project project) {
        return new StatusLabel(project);
    }

    @Override
    public void disposeWidget(@NotNull StatusBarWidget statusBarWidget) {
        Disposer.dispose(statusBarWidget);
    }

    @Override
    public boolean canBeEnabledOn(@NotNull StatusBar statusBar) {
        return true;
    }
}
