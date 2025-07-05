package usecases.aluguel;

import enums.StatusFerramenta;
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

    public void execute(List<Transacao> transacoes) {
        String idStr = this.inputHandler.notEmpty("Registrar Devolução", "Digite o ID da transação:");
        if (idStr == null) return;

        if (idStr.isEmpty()) {
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
            this.userInterface.showError("Erro: Esta transação já foi devolvida!");
            return;
        }

        String dataDevolucaoStr = this.inputHandler.notEmpty("Registrar Devolução", "Digite a data de devolução (ex.: DD/MM/AAAA HH:MM):");
        if (dataDevolucaoStr == null) return;

        if (dataDevolucaoStr.trim().isEmpty()) {
            this.userInterface.showError("Erro: Data de devolução é obrigatória!");
            return;
        }

        LocalDateTime dataDevolucao;
        try {
            dataDevolucao = LocalDateTime.parse(dataDevolucaoStr, DATETIME_FORMATTER);
        } catch (DateTimeParseException e) {
            this.userInterface.showError("Erro: Formato de data inválido! Use DD/MM/AAAA HH:MM.");
            return;
        }

        transacao.registrarDevolucao(dataDevolucao);
        transacao.getFerramenta().setStatus(StatusFerramenta.DISPONIVEL);

        String mensagem = "Devolução registrada com sucesso!\n" +
                "Custo total: R$" + String.format("%.2f", transacao.getCustoTotal()) + "\n" +
                "Multa por atraso: R$" + String.format("%.2f", transacao.getMulta());
        this.userInterface.showMessage("Sucesso", mensagem);
    }
}