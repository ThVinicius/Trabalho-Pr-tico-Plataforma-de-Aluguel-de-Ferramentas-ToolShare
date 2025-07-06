package utils;

import exceptions.FormatoDadosException;
import interfaces.IUserInterface;

public class InputHandler {
    private final IUserInterface userInterface;

    public InputHandler(IUserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public String notEmpty(String title, String message) throws FormatoDadosException {
        String value = this.userInterface.getInput(title, message);
        if (value == null) return null; // Usuário cancelou
        if (value.trim().isEmpty()) {
            throw new FormatoDadosException("Este campo não pode ser vazio.");
        }
        return value;
    }

    public Double getDouble(String title, String message) throws FormatoDadosException {
        String value = this.notEmpty(title, message);
        if (value == null) return null;
        try {
            return Double.parseDouble(value.replace(',', '.')); // Aceita vírgula e ponto
        } catch (NumberFormatException e) {
            throw new FormatoDadosException("Valor inválido. Digite um número decimal (ex: 10.50).");
        }
    }

    public Integer getInt(String title, String message) throws FormatoDadosException {
        String value = this.notEmpty(title, message);
        if (value == null) return null;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new FormatoDadosException("Valor inválido. Digite um número inteiro.");
        }
    }
}