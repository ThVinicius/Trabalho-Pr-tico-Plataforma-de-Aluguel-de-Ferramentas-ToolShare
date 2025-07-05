package enums;

public enum MenuFerramenta {
    CADASTRAR(1, "Cadastrar Nova Ferramenta"),
    BUSCAR_POR_NOME_OU_CODIGO(2, "Buscar por Nome/Código"),
    ALTERAR_PRECO(3, "Alterar Preço"),
    ALTERAR_STATUS(4, "Atualizar Status (Disponível/Manutenção)"),
    LISTAR_DISPONIVEIS(5, "Listar Disponíveis"),
    VOLTAR(6, "Voltar");

    private final int value;
    private final String operation;

    MenuFerramenta(int value, String operation) {
        this.value = value;
        this.operation = operation;
    }

    public static MenuFerramenta findByValue(int value) {
        for (MenuFerramenta option : values()) {
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