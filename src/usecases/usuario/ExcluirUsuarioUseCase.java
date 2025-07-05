package usecases.usuario;

import interfaces.IUserInterface;
import models.Transacao;
import models.Usuario;
import utils.InputHandler;

import java.util.List;

public class ExcluirUsuarioUseCase {
    private final IUserInterface userInterface;
    private final InputHandler inputHandler;

    public ExcluirUsuarioUseCase(IUserInterface userInterface) {
        this.userInterface = userInterface;
        this.inputHandler = new InputHandler(userInterface);
    }

    public void execute(List<Usuario> usuarios, List<Transacao> transacoes) {
        String cpf = this.inputHandler.notEmpty("Excluir Usuário", "Digite o CPF do usuário a excluir:");
        if (cpf == null) return;

        if (!Usuario.validarCPF(cpf)) {
            this.userInterface.showError("Erro: CPF inválido!");
            return;
        }

        Usuario usuario = null;
        for (Usuario u : usuarios) {
            if (u.getCpf().equals(cpf)) {
                usuario = u;
                break;
            }
        }

        if (usuario == null) {
            this.userInterface.showError("Erro: Usuário não encontrado!");
            return;
        }

        // Verifica aluguéis ativos
        for (Transacao t : transacoes) {
            if (t.getUsuario().equals(usuario) && t.getDataDevolucao() == null) {
                this.userInterface.showError("Erro: Usuário possui aluguéis ativos ou pendentes!");
                return;
            }
        }

        // Confirmação
        String mensagemConfirmacao = "Tem certeza que deseja excluir o usuário " + usuario.getNome() + " (CPF: " + usuario.getCpf() + ")?";
        String[] confirmOptions = {"Sim", "Não",};
        String confirmExclusion = this.userInterface.getInput("Confirmar Exclusão", mensagemConfirmacao, confirmOptions);
        if (confirmExclusion == null) return;
        if (confirmExclusion.equals("Sim")) {
            usuarios.remove(usuario);
            this.userInterface.showMessage("Sucesso", "Usuário excluído com sucesso!");
        } else {
            this.userInterface.showMessage("Informação", "Exclusão cancelada.");
        }
    }
}