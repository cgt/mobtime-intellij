package name.cgt.mobtimeintellij;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.impl.status.EditorBasedWidget;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;

public class StatusBarTimer extends EditorBasedWidget implements StatusBarWidget.TextPresentation {
    @NotNull
    public static final String id = "StatusBarTimer";

    @NotNull
    private String text = "Timer ready";

    protected StatusBarTimer(@NotNull Project project) {
        super(project);
    }

    @Override
    @NonNls
    @NotNull
    public String ID() {
        return id;
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
        super.install(statusBar);
        statusBar.updateWidget(ID());
        ServiceManager.getService(TimerService.class).addListener(new TimerService.Listener() {
            @Override
            public void onStarted() {
                text = "Timer started";
                statusBar.updateWidget(StatusBarTimer.this.ID());
            }
        });
    }

    @Override
    @Nullable
    public WidgetPresentation getPresentation() {
        return this;
    }

    @Override
    @NotNull
    @NlsContexts.Label
    public String getText() {
        return text;
    }

    @Override
    public float getAlignment() {
        return 0;
    }

    @Override
    @Nullable
    @NlsContexts.Tooltip
    public String getTooltipText() {
        return "Mobtime status";
    }

    @Override
    @Nullable
    public Consumer<MouseEvent> getClickConsumer() {
        return null;
    }
}