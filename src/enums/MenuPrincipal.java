package enums;

public enum MenuPrincipal {
    GERENCIAR_USUARIOS(1, "Gerenciar Usuários"),
    GERENCIAR_OPERACAO(2, "Gerenciar Ferramentas"),
    GERENCIAR_ALUGUEIS(3, "Gerenciar Aluguéis"),
    SAIR(4, "Sair");

    private final int value;
    private final String operation;

    MenuPrincipal(int value, String operation) {
        this.value = value;
        this.operation = operation;
    }

    public static MenuPrincipal findByValue(int value) {
        for (MenuPrincipal option : values()) {
            if (option.value == value) {
                return option;
            }
        }

        return null;
    }

    public int getValue() {
        return value;
    }

    public String getOperation() {
        return operation;
    }
}