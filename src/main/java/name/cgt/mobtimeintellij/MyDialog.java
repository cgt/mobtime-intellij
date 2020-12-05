package name.cgt.mobtimeintellij;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class MyDialog extends DialogWrapper {
    public MyDialog() {
        super(true);
        init();
        setTitle("My Fancy Dialog");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        final var panel = new JPanel();
        final var label = new JLabel("Looky here at my fancy dialog.");
        panel.add(label);
        return panel;
    }
}
