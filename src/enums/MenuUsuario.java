package enums;

public enum MenuUsuario {
    CADASTRAR_USUARIO(1, "Cadastrar Novo Usuário"),
    CONSULTAR_POR_CPF(2, "Consultar por CPF"),
    EDITAR_CADASTRO(3, "Editar Cadastro"),
    EXCLUIR_USUARIO(4, "Excluir Usuário"),
    LISTAR_TODOS(5, "Listar Todos"),
    VOLTAR(6, "Voltar");

    private final int value;
    private final String operation;

    MenuUsuario(int value, String operation) {
        this.value = value;
        this.operation = operation;
    }

    public static MenuUsuario findByValue(int value) {
        for (MenuUsuario option : values()) {
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