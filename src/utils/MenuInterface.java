package utils;

import javax.swing.*;

public class MenuInterface<T extends Enum<T>> {
    private final Class<T> enumType;
    private final String menuTitle;

    public MenuInterface(Class<T> enumType, String menuTitle) {
        this.enumType = enumType;
        this.menuTitle = menuTitle;
    }

    public T selectOption() {
        T selected = null;
        StringBuilder menu = new StringBuilder("======== " + menuTitle + " ======== \n");
        for (T opcao : enumType.getEnumConstants()) {
            try {
                int value = (int) opcao.getClass().getMethod("getValue").invoke(opcao);
                String operation = (String) opcao.getClass().getMethod("getOperation").invoke(opcao);
                menu.append(value).append(" - ").append(operation).append("\n");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro ao construir o menu: " + e.getMessage());
                return null;
            }
        }
        menu.append("\nDigite a opção desejada:");

        while (true) {
            String opcaoSelecionada = JOptionPane.showInputDialog(menu.toString());

            if (opcaoSelecionada == null) {
                // Usuário clicou em "Cancelar" ou fechou o diálogo
                return null;
            }

            try {
                int value = Integer.parseInt(opcaoSelecionada);
                selected = (T) enumType.getMethod("findByValue", int.class).invoke(null, value);
                if (selected != null) {
                    break;
                } else {
                    JOptionPane.showMessageDialog(null, "Opção inválida! Por favor, digite um dos números do menu.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Valor inválido! Por favor, digite um dos números do menu.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro ao processar a opção: " + e.getMessage());
            }
        }

        return selected;
    }
}