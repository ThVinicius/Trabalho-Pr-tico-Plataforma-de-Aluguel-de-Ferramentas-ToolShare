package usecases.aluguel;

import interfaces.IUserInterface;
import models.Transacao;

import java.util.List;

import static config.Config.DATETIME_FORMATTER;

public class ListarHistoricoAlugueisUseCase {
    private final IUserInterface userInterface;

    public ListarHistoricoAlugueisUseCase(IUserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void execute(List<Transacao> transacoes) {
        String title = "Histórico de Aluguéis";

        if (transacoes.isEmpty()) {
            this.userInterface.showMessage(title, "Nenhum aluguel registrado!");
            return;
        }

        StringBuilder mensagem = new StringBuilder("Histórico de Aluguéis:\n\n");
        for (Transacao t : transacoes) {
            mensagem.append("ID: ").append(t.getId()).append("\n")
                    .append("Usuário: ").append(t.getUsuario().getNome()).append(" (CPF: ").append(t.getUsuario().getCpf()).append(")\n")
                    .append("Ferramenta: ").append(t.getFerramenta().getNome()).append(" (Código: ").append(t.getFerramenta().getCodigo()).append(")\n")
                    .append("Data de início: ").append(t.getDataInicio().format(DATETIME_FORMATTER)).append("\n")
                    .append("Período: ").append(t.getPeriodo()).append(" dias\n")
                    .append("Data de término prevista: ").append(t.getDataFim().format(DATETIME_FORMATTER)).append("\n")
                    .append("Data de devolução: ").append(t.getDataDevolucao() != null ? t.getDataDevolucao().format(DATETIME_FORMATTER) : "Não devolvida").append("\n")
                    .append("Custo total: R$").append(String.format("%.2f", t.getCustoTotal())).append("\n")
                    .append("Multa: R$").append(String.format("%.2f", t.getMulta())).append("\n")
                    .append("------------------------\n");
        }
        this.userInterface.showMessage(title, mensagem.toString());
    }
}