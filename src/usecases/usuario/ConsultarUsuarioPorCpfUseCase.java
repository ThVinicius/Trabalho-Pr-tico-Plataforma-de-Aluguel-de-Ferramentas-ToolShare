package usecases.usuario;

import exceptions.FormatoDadosException;
import exceptions.ValidacaoException;
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

    public void execute(List<Usuario> usuarios) throws FormatoDadosException, ValidacaoException {
        String cpf = this.inputHandler.notEmpty("Consultar Usuário por CPF", "Digite o CPF:");
        if (cpf == null) return;

        Usuario usuario = usuarios.stream()
                .filter(u -> u.getCpf().equals(cpf))
                .findFirst()
                .orElseThrow(() -> new ValidacaoException("Erro: Usuário não encontrado!"));

        String mensagem = "Usuário encontrado:\n" +
                "Nome: " + usuario.getNome() + "\n" +
                "Contato: " + usuario.getContato() + "\n" +
                "CPF: " + usuario.getCpf();
        this.userInterface.showMessage("Resultado da Consulta", mensagem);
    }
}