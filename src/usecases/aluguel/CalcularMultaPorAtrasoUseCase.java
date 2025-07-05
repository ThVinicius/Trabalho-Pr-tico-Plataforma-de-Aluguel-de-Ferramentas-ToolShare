package usecases.aluguel;

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

    public void execute(List<Transacao> transacoes) {
        String idStr = this.inputHandler.notEmpty("Calcular Multa por Atraso", "Digite o ID da transação:");
        if (idStr == null || idStr.isEmpty()) {
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            this.userInterface.showError("Erro: ID inválido!");
            return;
        }

        Transacao transacao = null;
        for (Transacao t : transacoes) {
            if (t.getId() == id) {
                transacao = t;
                break;
            }
        }

        if (transacao == null) {
            this.userInterface.showError("Erro: Transação não encontrada!");
            return;
        }

        if (transacao.getDataDevolucao() != null) {
            this.userInterface.showError("Erro: Esta transação já foi devolvida! Multa: R$" + String.format("%.2f", transacao.getMulta()));
            return;
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