package usecases.aluguel;

import interfaces.IUserInterface;
import models.Transacao;
import models.Usuario;
import utils.InputHandler;

import java.time.LocalDateTime;
import java.util.List;

import static config.Config.DATETIME_FORMATTER;

public class ConsultarAluguelAtivoUseCase {
    private final IUserInterface userInterface;
    private final InputHandler inputHandler;

    public ConsultarAluguelAtivoUseCase(IUserInterface userInterface) {
        this.userInterface = userInterface;
        this.inputHandler = new InputHandler(userInterface);
    }

    public void execute(List<Usuario> usuarios, List<Transacao> transacoes) {
        String[] opcoes = {"Listar todos os aluguéis ativos", "Filtrar por CPF do usuário"};
        String escolha = this.userInterface.getInput(
                "Consultar Aluguel Ativo", "Selecione uma opção:", opcoes
        );
        if (escolha == null) return;

        StringBuilder resultado = new StringBuilder();
        boolean encontrou = false;

        if (escolha.equals(opcoes[0])) {
            resultado.append("Aluguéis Ativos:\n");
            for (Transacao t : transacoes) {
                if (t.getFerramenta().getStatus().equals("Alugada")) {
                    resultado.append(formatarTransacao(t));
                    encontrou = true;
                }
            }
        } else {
            String cpf = this.inputHandler.notEmpty("Consultar Aluguel Ativo", "Digite o CPF do usuário:");
            if (cpf == null) return;

            if (!Usuario.validarCPF(cpf)) {
                this.userInterface.showError("Erro: CPF inválido!");
                return;
            }

            resultado.append("Aluguéis Ativos para CPF ").append(cpf).append(":\n");
            for (Transacao t : transacoes) {
                if (t.getFerramenta().getStatus().equals("Alugada") && t.getUsuario().getCpf().equals(cpf)) {
                    resultado.append(formatarTransacao(t));
                    encontrou = true;
                }
            }
        }

        if (!encontrou) {
            resultado.append("Nenhum aluguel ativo encontrado.");
        }

        this.userInterface.showMessage("Resultado da Consulta", resultado.toString());
    }

    private String formatarTransacao(Transacao t) {
        LocalDateTime dataDevolucao = t.getDataInicio().plusDays(t.getPeriodo());
        return String.format(
                "ID: %d\nUsuário: %s\nFerramenta: %s (Código: %d)\nData de Início: %s\nPeríodo: %d dias\nDevolução Esperada: %s\n\n",
                t.getId(),
                t.getUsuario().getNome(),
                t.getFerramenta().getNome(),
                t.getFerramenta().getCodigo(),
                t.getDataInicio().format(DATETIME_FORMATTER),
                t.getPeriodo(),
                dataDevolucao.format(DATETIME_FORMATTER)
        );
    }
}