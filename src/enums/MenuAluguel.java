package enums;

public enum MenuAluguel {
    REALIZAR_ALUGUEL(1, "Realizar Aluguel"),
    REGISTRAR_DEVOLUCAO(2, "Registrar Devolução"),
    CONSULTAR_ALUGUEL_ATIVO(3, "Consultar Aluguel Ativo"),
    CALCULAR_MULTA_POR_ATRASO(4, "Calcular Multa por Atraso"),
    LISTAR_HISTORICO(5, "Listar Histórico"),
    VOLTAR(6, "Voltar");

    private final int value;
    private final String operation;

    MenuAluguel(int value, String operation) {
        this.value = value;
        this.operation = operation;
    }

    public static MenuAluguel findByValue(int value) {
        for (MenuAluguel option : values()) {
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