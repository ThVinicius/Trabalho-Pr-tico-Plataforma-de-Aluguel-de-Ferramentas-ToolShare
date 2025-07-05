package usecases.usuario;

import interfaces.IUserInterface;
import models.Usuario;
import utils.InputHandler;

import java.util.List;

public class ConsultarUsuarioPorCpfUseCase {
    private final IUserInterface userInterface;
    private final InputHandler inputHandler;

    public ConsultarUsuarioPorCpfUseCase(IUserInterface userInterface) {
        this.userInterface = userInterface;
        this.inputHandler = new InputHandler(userInterface);
    }

    public void execute(List<Usuario> usuarios) {
        String cpf = this.inputHandler.notEmpty("Consultar Usuário por CPF", "Digite o CPF:");
        if (cpf == null) return;

        for (Usuario usuario : usuarios) {
            if (usuario.getCpf().equals(cpf)) {
                String mensagem = "Usuário encontrado:\n" +
                        "Nome: " + usuario.getNome() + "\n" +
                        "Contato: " + usuario.getContato() + "\n" +
                        "CPF: " + usuario.getCpf();
                this.userInterface.showMessage("Resultado da Consulta", mensagem);
                return;
            }
        }

        this.userInterface.showError("Erro: Usuário não encontrado!");
    }
}