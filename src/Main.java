import interfaces.IUserInterface;
import views.InterfaceOption;
import views.MenuInterface;

public class Main {
    public static void main(String[] args) {
        InterfaceOption interfaceOption = new InterfaceOption();
        IUserInterface userInterface = interfaceOption.execute();
        if (userInterface == null) return;

        MenuInterface menu = new MenuInterface(userInterface);
        menu.execute();
    }
}