package enums;

public enum InterfaceOption {
    TERMINAL(1, "Terminal"),
    J_OPTION_PANE(2, "JOptionPane"),
    SAIR(3, "Sair");

    private final int value;
    private final String operation;

    InterfaceOption(int value, String operation) {
        this.value = value;
        this.operation = operation;
    }

    public static InterfaceOption findByValue(int value) {
        for (InterfaceOption option : values()) {
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