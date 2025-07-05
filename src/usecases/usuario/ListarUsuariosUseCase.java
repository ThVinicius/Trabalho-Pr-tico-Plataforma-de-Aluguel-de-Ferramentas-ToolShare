package usecases.usuario;

import interfaces.IUserInterface;
import models.Usuario;

import java.util.List;

public class ListarUsuariosUseCase {
    private final IUserInterface userInterface;

    public ListarUsuariosUseCase(IUserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void execute(List<Usuario> usuarios) {
        String title = "Lista de Usuários";

        if (usuarios.isEmpty()) {
            this.userInterface.showMessage(title, "Nenhum usuário cadastrado!");
            return;
        }

        StringBuilder mensagem = new StringBuilder("Lista de Usuários:\n\n");
        for (Usuario usuario : usuarios) {
            mensagem.append("Nome: ").append(usuario.getNome()).append("\n")
                    .append("Contato: ").append(usuario.getContato()).append("\n")
                    .append("CPF: ").append(usuario.getCpf()).append("\n")
                    .append("------------------------\n");
        }
        this.userInterface.showMessage(title, mensagem.toString());
    }
}