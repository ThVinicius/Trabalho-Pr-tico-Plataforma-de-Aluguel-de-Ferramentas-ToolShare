package interfaces;

import enums.MenuAluguel;
import enums.MenuFerramenta;
import enums.MenuType;
import enums.MenuUsuario;
import utils.MenuInterface;

import javax.swing.*;

public class JOptionPaneUI implements IUserInterface {
    private final MenuInterface<enums.MenuPrincipal> menuPrincipal = new MenuInterface<>(enums.MenuPrincipal.class, "TOOLSHARE");
    private final MenuInterface<MenuUsuario> menuUsuario = new MenuInterface<>(MenuUsuario.class, "GERENCIAR USUÁRIOS");
    private final MenuInterface<MenuFerramenta> menuFerramenta = new MenuInterface<>(MenuFerramenta.class, "GERENCIAR FERRAMENTAS");
    private final MenuInterface<MenuAluguel> menuAluguel = new MenuInterface<>(MenuAluguel.class, "GERENCIAR ALUGUÉIS");

    @Override
    public Integer showMenu(MenuType type) {
        Integer value = null;
        
        if (type == null) return null;

        switch (type) {
            case PRINCIPAL:
                enums.MenuPrincipal selectedPrincipal = menuPrincipal.selectOption();
                if (selectedPrincipal != null) {
                    value = selectedPrincipal.getValue();
                }
                break;

            case USER:
                MenuUsuario selectedUsuario = menuUsuario.selectOption();
                if (selectedUsuario != null) {
                    value = selectedUsuario.getValue();
                }
                break;

            case TOOL:
                MenuFerramenta selectedFerramenta = menuFerramenta.selectOption();
                if (selectedFerramenta != null) {
                    value = selectedFerramenta.getValue();
                }
                break;

            case TRANSACTION:
                MenuAluguel selectedAluguel = menuAluguel.selectOption();
                if (selectedAluguel != null) {
                    value = selectedAluguel.getValue();
                }
                break;
        }

        return value;
    }

    @Override
    public String getInput(String title, String message) {
        return JOptionPane.showInputDialog(null, message, title, JOptionPane.QUESTION_MESSAGE);
    }

    @Override
    public String getInput(String title, String message, String[] options) {
        return (String) JOptionPane.showInputDialog(
                null, message, title, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
    }


    @Override
    public void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
