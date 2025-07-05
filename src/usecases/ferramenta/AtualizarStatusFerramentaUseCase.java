package usecases.ferramenta;

import enums.StatusFerramenta;
import interfaces.IUserInterface;
import models.Ferramenta;
import models.Usuario;
import utils.InputHandler;

import java.util.List;

public class AtualizarStatusFerramentaUseCase {
    private final IUserInterface userInterface;
    private final InputHandler inputHandler;

    public AtualizarStatusFerramentaUseCase(IUserInterface userInterface) {
        this.userInterface = userInterface;
        this.inputHandler = new InputHandler(userInterface);
    }

    public void execute(List<Usuario> usuarios, List<Ferramenta> ferramentas) {
        String cpf = this.inputHandler.notEmpty("Atualizar Status", "Digite o CPF do usuário:");
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

        String codigoStr = this.inputHandler.notEmpty("Atualizar Status", "Digite o código da ferramenta:");
        if (codigoStr == null) return;
        int codigo;
        try {
            codigo = Integer.parseInt(codigoStr);
        } catch (NumberFormatException e) {
            this.userInterface.showError("Erro: Código inválido!");
            return;
        }

        Ferramenta ferramenta = null;
        for (Ferramenta f : ferramentas) {
            if (f.getCodigo() == codigo) {
                ferramenta = f;
                break;
            }
        }

        if (ferramenta == null) {
            this.userInterface.showError("Erro: Ferramenta não encontrada!");
            return;
        }

        if (!ferramenta.getProprietario().equals(usuario)) {
            this.userInterface.showError("Erro: Apenas o proprietário pode alterar o status da ferramenta!");
            return;
        }

        if (ferramenta.getStatus().equals("Alugada")) {
            this.userInterface.showError("Erro: Ferramenta alugada não pode ter o status alterado!");
            return;
        }

        String[] statusOpcoes = {"Disponível", "Em Manutenção"};
        String novoStatus = this.userInterface.getInput(
                "Atualizar Status", "Selecione o novo status:", statusOpcoes);
        if (novoStatus == null) {
            return;
        }

        StatusFerramenta statusFerramenta = StatusFerramenta.EM_MANUTENCAO;
        if (novoStatus.equals(statusOpcoes[0])) statusFerramenta = StatusFerramenta.DISPONIVEL;

        ferramenta.setStatus(statusFerramenta);
        this.userInterface.showMessage("Sucesso", "Status atualizado com sucesso!");
    }
}
