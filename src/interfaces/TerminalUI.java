package interfaces;

import enums.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TerminalUI implements IUserInterface {
    private final Scanner scanner = new Scanner(System.in);
    
    @Override
    public Integer showMenu(MenuType type) {
        if (type == null) {
            return null;
        }

        Class<? extends Enum<?>> enumClass;
        String menuTitle;

        switch (type) {
            case PRINCIPAL:
                enumClass = MenuPrincipal.class;
                menuTitle = "MENU PRINCIPAL";
                break;
            case USER:
                enumClass = MenuUsuario.class;
                menuTitle = "GERENCIAR USUÁRIOS";
                break;
            case TOOL:
                enumClass = MenuFerramenta.class;
                menuTitle = "GERENCIAR FERRAMENTAS";
                break;
            case TRANSACTION:
                enumClass = MenuAluguel.class;
                menuTitle = "GERENCIAR ALUGUÉIS";
                break;
            default:
                showError("Tipo de menu desconhecido.");
                return null;
        }

        StringBuilder menu = new StringBuilder("\n======== " + menuTitle + " ======== \n");
        for (Enum<?> option : enumClass.getEnumConstants()) {
            try {
                // Usa reflexão para invocar os métodos getValue() e getOperation() do enum
                int value = (int) option.getClass().getMethod("getValue").invoke(option);
                String operation = (String) option.getClass().getMethod("getOperation").invoke(option);
                menu.append(String.format("%d - %s%n", value, operation));
            } catch (Exception e) {
                showError("Erro ao construir o menu: " + e.getMessage());
                return null;
            }
        }
        menu.append("\nDigite a opção desejada:");
        System.out.println(menu);

        while (true) {
            try {
                System.out.print("> ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                Object selectedOption = enumClass.getMethod("findByValue", int.class).invoke(null, choice);

                if (selectedOption != null) {
                    return choice;
                } else {
                    showMessage("Aviso", "Opção inválida! Por favor, digite um dos números do menu.");
                    System.out.print("> ");
                }
            } catch (InputMismatchException e) {
                showMessage("Aviso", "Valor inválido! Por favor, digite um número inteiro.");
                scanner.nextLine();
            } catch (Exception e) {
                showError("Erro ao processar a opção: " + e.getMessage());
                return null;
            }
        }
    }

    @Override
    public String getInput(String title, String message) {
        System.out.println("\n--- " + title + " ---");
        System.out.println(message);
        System.out.print("> ");
        return scanner.nextLine();
    }

    @Override
    public String getInput(String title, String message, String[] options) {
        System.out.println("\n--- " + title + " ---");
        System.out.println(message);

        for (int i = 0; i < options.length; i++) {
            System.out.printf("%d - %s%n", i + 1, options[i]);
        }

        while (true) {
            try {
                System.out.print("> ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice > 0 && choice <= options.length) {
                    return options[choice - 1];
                } else {
                    showMessage("Aviso", "Opção inválida! Escolha um número entre 1 e " + options.length + ".");
                }
            } catch (InputMismatchException e) {
                showMessage("Aviso", "Entrada inválida! Por favor, digite um número.");
                scanner.nextLine();
            }
        }
    }

    @Override
    public void showMessage(String title, String message) {
        System.out.printf("\n[INFO: %s] %s%n", title, message);
    }

    @Override
    public void showError(String message) {
        System.err.printf("\n[ERRO] %s%n", message);
    }
}