package utils;

import interfaces.IUserInterface;

public class InputHandler {
    private final IUserInterface userInterface;

    public InputHandler(IUserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public String notEmpty(String title, String message) {
        String value = this.userInterface.getInput(title, message);

        if (value == null) return null;

        if (!value.isEmpty()) {
            return value;
        }

        this.userInterface.showError("Erro: Esse campo não pode ser vazio");

        return this.notEmpty(title, message);
    }

    public Double getDouble(String title, String message) {
        String value = this.userInterface.getInput(title, message);

        if (value == null) return null;

        if (value.isEmpty()) {
            this.userInterface.showError("Erro: Esse campo não pode ser vazio");
            return this.getDouble(title, message);
        }

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            this.userInterface.showError("Erro: Digite um número decimal válido");
            return this.getDouble(title, message);
        }
    }

    public Integer getInt(String title, String message) {
        String value = this.userInterface.getInput(title, message);

        if (value == null) return null;

        if (value.isEmpty()) {
            this.userInterface.showError("Erro: Esse campo não pode ser vazio");
            return this.getInt(title, message);
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            this.userInterface.showError("Erro: Digite um número inteiro válido");
            return this.getInt(title, message);
        }
    }
}