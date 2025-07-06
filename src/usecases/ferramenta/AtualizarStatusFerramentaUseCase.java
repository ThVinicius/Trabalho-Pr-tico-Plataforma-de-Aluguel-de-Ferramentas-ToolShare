package usecases.ferramenta;

import enums.StatusFerramenta;
import exceptions.FerramentaIndisponivelException;
import exceptions.FormatoDadosException;
import exceptions.ValidacaoException;
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

    public void execute(List<Usuario> usuarios, List<Ferramenta> ferramentas) throws FormatoDadosException, ValidacaoException, FerramentaIndisponivelException {
        String cpf = this.inputHandler.notEmpty("Atualizar Status", "Digite o CPF do usuário:");
        if (cpf == null) return;

        if (!Usuario.validarCPF(cpf)) {
            throw new FormatoDadosException("Erro: CPF inválido!");
        }

        Usuario usuario = usuarios.stream()
                .filter(u -> u.getCpf().equals(cpf))
                .findFirst()
                .orElseThrow(() -> new ValidacaoException("Erro: Usuário não encontrado!"));

        Integer codigo = this.inputHandler.getInt("Atualizar Status", "Digite o código da ferramenta:");
        if (codigo == null) return;

        Ferramenta ferramenta = ferramentas.stream()
                .filter(f -> f.getCodigo() == codigo)
                .findFirst()
                .orElseThrow(() -> new ValidacaoException("Erro: Ferramenta não encontrada!"));

        if (!ferramenta.getProprietario().equals(usuario)) {
            throw new ValidacaoException("Erro: Apenas o proprietário pode alterar o status da ferramenta!");
        }

        if (ferramenta.getStatus() == StatusFerramenta.ALUGADA) {
            throw new FerramentaIndisponivelException("Erro: Ferramenta alugada não pode ter o status alterado!");
        }

        String[] statusOpcoes = {"Disponível", "Em Manutenção"};
        String novoStatusStr = this.userInterface.getInput("Atualizar Status", "Selecione o novo status:", statusOpcoes);
        if (novoStatusStr == null) {
            return;
        }

        StatusFerramenta novoStatus = "Disponível".equals(novoStatusStr) ? StatusFerramenta.DISPONIVEL : StatusFerramenta.EM_MANUTENCAO;
        ferramenta.setStatus(novoStatus);
        this.userInterface.showMessage("Sucesso", "Status atualizado com sucesso!");
    }
}