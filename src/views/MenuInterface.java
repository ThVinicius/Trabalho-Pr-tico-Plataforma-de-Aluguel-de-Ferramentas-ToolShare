package views;

import enums.MenuPrincipal;
import enums.MenuType;
import interfaces.IUserInterface;
import models.Plataforma;

public class MenuInterface {
    private final IUserInterface userInterface;
    private final Plataforma plataforma;

    public MenuInterface(IUserInterface userInterface) {
        this.userInterface = userInterface;
        this.plataforma = new Plataforma(userInterface);
    }

    public void execute() {
        Integer selected = this.userInterface.showMenu(MenuType.PRINCIPAL);

        if (selected == null) {
            this.plataforma.salvarDados();
            return;
        }

        switch (selected) {
            case 1 -> this.plataforma.gerenciarUsuarios();
            case 2 -> this.plataforma.gerenciarFerramentas();
            case 3 -> this.plataforma.gerenciarAlugueis();
            case 4 -> this.plataforma.salvarDados();
        }

        if (MenuPrincipal.findByValue(selected) != MenuPrincipal.SAIR) {
            this.execute();
        }
    }
}



