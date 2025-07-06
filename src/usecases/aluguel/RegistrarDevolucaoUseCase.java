package usecases.aluguel;

import enums.StatusFerramenta;
import exceptions.FormatoDadosException;
import exceptions.ValidacaoException;
import interfaces.IUserInterface;
import models.Transacao;
import utils.InputHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import static config.Config.DATETIME_FORMATTER;

public class RegistrarDevolucaoUseCase {
    private final IUserInterface userInterface;
    private final InputHandler inputHandler;

    public RegistrarDevolucaoUseCase(IUserInterface userInterface) {
        this.userInterface = userInterface;
        this.inputHandler = new InputHandler(userInterface);
    }

    public void execute(List<Transacao> transacoes) throws FormatoDadosException, ValidacaoException {
        Integer id = this.inputHandler.getInt("Registrar Devolução", "Digite o ID da transação:");
        if (id == null) return;

        Transacao transacao = transacoes.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ValidacaoException("Erro: Transação não encontrada!"));

        if (transacao.getDataDevolucao() != null) {
            throw new ValidacaoException("Erro: Esta transação já foi devolvida!");
        }

        String dataDevolucaoStr = this.inputHandler.notEmpty("Registrar Devolução", "Digite a data de devolução (ex.: DD/MM/AAAA HH:MM):");
        if (dataDevolucaoStr == null) return;

        LocalDateTime dataDevolucao;
        try {
            dataDevolucao = LocalDateTime.parse(dataDevolucaoStr, DATETIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new FormatoDadosException("Erro: Formato de data inválido! Use DD/MM/AAAA HH:MM.");
        }

        transacao.registrarDevolucao(dataDevolucao);
        transacao.getFerramenta().setStatus(StatusFerramenta.DISPONIVEL);

        String mensagem = "Devolução registrada com sucesso!\n" +
                "Custo total: R$" + String.format("%.2f", transacao.getCustoTotal()) + "\n" +
                "Multa por atraso: R$" + String.format("%.2f", transacao.getMulta());
        this.userInterface.showMessage("Sucesso", mensagem);
    }
}