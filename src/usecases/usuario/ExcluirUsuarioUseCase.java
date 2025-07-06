package usecases.usuario;

import exceptions.FormatoDadosException;
import exceptions.ValidacaoException;
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

    public void execute(List<Usuario> usuarios, List<Transacao> transacoes) throws FormatoDadosException, ValidacaoException {
        String cpf = this.inputHandler.notEmpty("Excluir Usuário", "Digite o CPF do usuário a excluir:");
        if (cpf == null) return;

        if (!Usuario.validarCPF(cpf)) {
            throw new FormatoDadosException("Erro: CPF inválido!");
        }

        Usuario usuario = usuarios.stream()
                .filter(u -> u.getCpf().equals(cpf))
                .findFirst()
                .orElseThrow(() -> new ValidacaoException("Erro: Usuário não encontrado!"));


        boolean temAluguelAtivo = transacoes.stream()
                .anyMatch(t -> t.getUsuario().equals(usuario) && t.getDataDevolucao() == null);

        if (temAluguelAtivo) {
            throw new ValidacaoException("Erro: Usuário possui aluguéis ativos ou pendentes e não pode ser excluído!");
        }

       
        String mensagemConfirmacao = "Tem certeza que deseja excluir o usuário " + usuario.getNome() + " (CPF: " + usuario.getCpf() + ")?";
        String[] confirmOptions = {"Sim", "Não"};
        String confirmExclusion = this.userInterface.getInput("Confirmar Exclusão", mensagemConfirmacao, confirmOptions);
        if (confirmExclusion != null && confirmExclusion.equals("Sim")) {
            usuarios.remove(usuario);
            this.userInterface.showMessage("Sucesso", "Usuário excluído com sucesso!");
        } else {
            this.userInterface.showMessage("Informação", "Exclusão cancelada.");
        }
    }
}