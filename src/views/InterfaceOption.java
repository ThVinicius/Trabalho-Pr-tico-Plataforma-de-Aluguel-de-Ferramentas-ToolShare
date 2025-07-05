package views;

import interfaces.IUserInterface;
import interfaces.JOptionPaneUI;
import interfaces.TerminalUI;
import utils.MenuInterface;

public class InterfaceOption {
    private final MenuInterface<enums.InterfaceOption> menu = new MenuInterface<>(enums.InterfaceOption.class, "Selecione uma Interface");

    public IUserInterface execute() {
        IUserInterface userInterface = null;
        enums.InterfaceOption selected = this.menu.selectOption();

        if (selected == null) return null;

        switch (selected.getValue()) {
            case 1 -> userInterface = new TerminalUI();
            case 2 -> userInterface = new JOptionPaneUI();
        }

        return userInterface;
    }
}
