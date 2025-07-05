package interfaces;

import enums.MenuType;

public interface IUserInterface {
    Integer showMenu(MenuType type);

    String getInput(String title, String message);

    String getInput(String title, String message, String[] options);

    void showMessage(String title, String message);

    void showError(String message);
}
