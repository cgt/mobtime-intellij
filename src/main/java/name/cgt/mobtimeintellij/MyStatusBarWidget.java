package name.cgt.mobtimeintellij;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.impl.status.EditorBasedWidget;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;

public class MyStatusBarWidget extends EditorBasedWidget implements StatusBarWidget.TextPresentation {
    private final Mobtime mobtime;
    private String myText = "MY STATUS BAR LABEL";

    public MyStatusBarWidget(@NotNull Project project) {
        super(project);
        mobtime = new Mobtime();
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
        super.install(statusBar);
        myStatusBar.updateWidget(ID());
        final var app = ApplicationManager.getApplication();
        app.executeOnPooledThread(() ->
          mobtime.connect("idea", message ->
            app.invokeLater(() -> {
                myText = message;
                myStatusBar.updateWidget(ID());
            })
          )
        );
    }

    @Override
    public @NotNull String ID() {
        return "mystatusbarwidgetid";
    }

    @Override
    public @Nullable WidgetPresentation getPresentation() {
        return this;
    }

    @Override
    public @NotNull String getText() {
        return myText;
    }

    @Override
    public @Nullable String getTooltipText() {
        return "MY STATUS BAR TOOLTIP";
    }

    @Override
    public @Nullable Consumer<MouseEvent> getClickConsumer() {
        return null;
    }

    @Override
    public float getAlignment() {
        return 0;
    }
}
