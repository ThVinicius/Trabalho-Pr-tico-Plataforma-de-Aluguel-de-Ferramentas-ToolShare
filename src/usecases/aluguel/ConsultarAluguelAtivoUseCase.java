package usecases.aluguel;

import exceptions.FormatoDadosException;
import interfaces.IUserInterface;
import models.Transacao;
import models.Usuario;
import utils.InputHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static config.Config.DATETIME_FORMATTER;

public class ConsultarAluguelAtivoUseCase {
    private final IUserInterface userInterface;
    private final InputHandler inputHandler;

    public ConsultarAluguelAtivoUseCase(IUserInterface userInterface) {
        this.userInterface = userInterface;
        this.inputHandler = new InputHandler(userInterface);
    }

    public void execute(List<Usuario> usuarios, List<Transacao> transacoes) throws FormatoDadosException {
        String[] opcoes = {"Listar todos os aluguéis ativos", "Filtrar por CPF do usuário"};
        String escolha = this.userInterface.getInput(
                "Consultar Aluguel Ativo", "Selecione uma opção:", opcoes
        );
        if (escolha == null) return;

        List<Transacao> alugueisAtivos;

        if (escolha.equals(opcoes[0])) {
            alugueisAtivos = transacoes.stream()
                    .filter(t -> t.getDataDevolucao() == null)
                    .collect(Collectors.toList());
        } else {
            String cpf = this.inputHandler.notEmpty("Consultar Aluguel Ativo", "Digite o CPF do usuário:");
            if (cpf == null) return;

            if (!Usuario.validarCPF(cpf)) {
                throw new FormatoDadosException("Erro: CPF inválido!");
            }
            alugueisAtivos = transacoes.stream()
                    .filter(t -> t.getDataDevolucao() == null && t.getUsuario().getCpf().equals(cpf))
                    .collect(Collectors.toList());
        }

        if (alugueisAtivos.isEmpty()) {
            this.userInterface.showMessage("Resultado da Consulta", "Nenhum aluguel ativo encontrado.");
            return;
        }

        StringBuilder resultado = new StringBuilder("Aluguéis Ativos:\n\n");
        for (Transacao t : alugueisAtivos) {
            resultado.append(formatarTransacao(t));
        }
        this.userInterface.showMessage("Resultado da Consulta", resultado.toString());
    }

    private String formatarTransacao(Transacao t) {
        LocalDateTime dataDevolucaoEsperada = t.getDataInicio().plusDays(t.getPeriodo());
        return String.format(
                "ID: %d\nUsuário: %s\nFerramenta: %s (Cód: %d)\nData de Início: %s\nPeríodo: %d dias\nDevolução Esperada: %s\n\n",
                t.getId(),
                t.getUsuario().getNome(),
                t.getFerramenta().getNome(),
                t.getFerramenta().getCodigo(),
                t.getDataInicio().format(DATETIME_FORMATTER),
                t.getPeriodo(),
                dataDevolucaoEsperada.format(DATETIME_FORMATTER)
        );
    }
}