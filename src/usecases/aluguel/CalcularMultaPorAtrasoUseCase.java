package usecases.aluguel;

import exceptions.FormatoDadosException;
import exceptions.ValidacaoException;
import interfaces.IUserInterface;
import models.Transacao;
import utils.InputHandler;

import java.time.LocalDateTime;
import java.util.List;

import static config.Config.DATETIME_FORMATTER;

public class CalcularMultaPorAtrasoUseCase {
    private final IUserInterface userInterface;
    private final InputHandler inputHandler;

    public CalcularMultaPorAtrasoUseCase(IUserInterface userInterface) {
        this.userInterface = userInterface;
        this.inputHandler = new InputHandler(userInterface);
    }

    public void execute(List<Transacao> transacoes) throws FormatoDadosException, ValidacaoException {
        Integer id = this.inputHandler.getInt("Calcular Multa por Atraso", "Digite o ID da transação:");
        if (id == null) {
            return;
        }

        Transacao transacao = transacoes.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ValidacaoException("Erro: Transação não encontrada!"));

        if (transacao.getDataDevolucao() != null) {
            throw new ValidacaoException("Erro: Esta transação já foi devolvida! Multa: R$" + String.format("%.2f", transacao.getMulta()));
        }

        LocalDateTime dataAtual = LocalDateTime.now();
        if (dataAtual.isBefore(transacao.getDataFim()) || dataAtual.isEqual(transacao.getDataFim())) {
            this.userInterface.showMessage("Calcular Multa por Atraso", "Nenhuma multa acumulada até o momento!");
            return;
        }

        long diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(transacao.getDataFim(), dataAtual);
        double multa = transacao.getFerramenta().getPrecoPorDia() * 0.1 * diasAtraso;

        String mensagem = "Multa acumulada até " + dataAtual.format(DATETIME_FORMATTER) + ":\n" +
                "Dias de atraso: " + diasAtraso + "\n" +
                "Multa: R$" + String.format("%.2f", multa);
        this.userInterface.showMessage("Calcular Multa por Atraso", mensagem);
    }
}