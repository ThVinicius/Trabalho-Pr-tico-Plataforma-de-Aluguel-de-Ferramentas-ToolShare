package usecases.relatorio;

import exceptions.FormatoDadosException;
import interfaces.IUserInterface;
import models.Ferramenta;
import models.Transacao;
import utils.InputHandler;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class GerarRelatorioFaturamentoUseCase {
    private final IUserInterface userInterface;
    private final InputHandler inputHandler;
    private final String title = "Relatório de Faturamento";

    public GerarRelatorioFaturamentoUseCase(IUserInterface userInterface) {
        this.userInterface = userInterface;
        this.inputHandler = new InputHandler(userInterface);
    }

    public void execute(List<Transacao> transacoes) throws FormatoDadosException {
        Integer ano = inputHandler.getInt(title, "Digite o ano para o relatório (ex: " + Year.now().getValue() + "):");
        if (ano == null) return;

        Integer mes = inputHandler.getInt(title, "Digite o mês para o relatório (1 a 12):");
        if (mes < 1 || mes > 12) {
            throw new FormatoDadosException("Mês inválido. Deve ser um número entre 1 e 12.");
        }

        List<Transacao> transacoesDoPeriodo = filtrarTransacoesConcluidas(transacoes, ano, mes);

        if (transacoesDoPeriodo.isEmpty()) {
            userInterface.showMessage(title, "Nenhuma transação concluída encontrada para " + mes + "/" + ano + ".");
            return;
        }

        String relatorioCompleto = construirRelatorio(transacoesDoPeriodo, ano, mes);

        userInterface.showMessage(title, relatorioCompleto);
        salvarRelatorioSeDesejado(relatorioCompleto, ano, mes);
    }

    private List<Transacao> filtrarTransacoesConcluidas(List<Transacao> todasTransacoes, int ano, int mes) {
        return todasTransacoes.stream()
                .filter(t -> t.getDataDevolucao() != null)
                .filter(t -> t.getDataDevolucao().getYear() == ano && t.getDataDevolucao().getMonthValue() == mes)
                .toList();
    }


    private String construirRelatorio(List<Transacao> transacoesDoPeriodo, int ano, int mes) {
        StringBuilder relatorio = new StringBuilder();

        adicionarCabecalho(relatorio, ano, mes);
        adicionarMetricasGerais(relatorio, transacoesDoPeriodo);
        adicionarFaturamentoPorCategoria(relatorio, transacoesDoPeriodo);
        adicionarFerramentasMaisAlugadas(relatorio, transacoesDoPeriodo);
        adicionarRodape(relatorio);

        return relatorio.toString();
    }

    private void adicionarCabecalho(StringBuilder relatorio, int ano, int mes) {
        Month mesEnum = Month.of(mes);

        Locale localePtBr = new Locale("pt", "BR");
        String nomeMes = mesEnum.getDisplayName(TextStyle.FULL, localePtBr).toUpperCase();

        relatorio.append("========================================\n");
        relatorio.append("  RELATÓRIO DE FATURAMENTO - ").append(nomeMes).append("/").append(ano).append("\n");
        relatorio.append("========================================\n\n");
    }

    private void adicionarMetricasGerais(StringBuilder relatorio, List<Transacao> transacoes) {
        double receitaTotal = transacoes.stream().mapToDouble(Transacao::getCustoTotal).sum();
        double totalMultas = transacoes.stream().mapToDouble(Transacao::getMulta).sum();

        relatorio.append(String.format("Receita Total: R$ %.2f%n", receitaTotal));
        relatorio.append(String.format("Total em Multas: R$ %.2f%n\n", totalMultas));
    }

    private void adicionarFaturamentoPorCategoria(StringBuilder relatorio, List<Transacao> transacoes) {
        Map<String, Double> faturamentoPorCategoria = transacoes.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getFerramenta().getNomeCategoria(),
                        Collectors.summingDouble(Transacao::getCustoTotal)
                ));

        relatorio.append("--- Faturamento por Categoria ---\n");
        faturamentoPorCategoria.forEach((categoria, valor) ->
                relatorio.append(String.format("%-10s: R$ %.2f%n", categoria, valor))
        );
        relatorio.append("\n");
    }

    private void adicionarFerramentasMaisAlugadas(StringBuilder relatorio, List<Transacao> transacoes) {
        Map<Ferramenta, Long> contagemDeAlugueis = transacoes.stream()
                .collect(Collectors.groupingBy(
                        Transacao::getFerramenta,
                        Collectors.counting()
                ));

        relatorio.append("--- Ferramentas Mais Alugadas ---\n");
        contagemDeAlugueis.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5) // Limita ao Top 5
                .forEach(entry -> {
                    Ferramenta f = entry.getKey();
                    long count = entry.getValue();
                    relatorio.append(String.format("%d vez(es) - %s (Cód: %d)%n", count, f.getNome(), f.getCodigo()));
                });
        relatorio.append("\n");
    }

    private void adicionarRodape(StringBuilder relatorio) {
        relatorio.append("========================================\n");
    }

    private void salvarRelatorioSeDesejado(String conteudo, int ano, int mes) {
        String[] opcoes = {"Sim", "Não"};
        String escolha = userInterface.getInput(title, "Deseja salvar este relatório em um arquivo de texto?", opcoes);

        if ("Sim".equalsIgnoreCase(escolha)) {
            String nomeArquivo = String.format("relatorio_%d_%02d.txt", ano, mes);
            try (FileWriter writer = new FileWriter(nomeArquivo)) {
                writer.write(conteudo);
                userInterface.showMessage("Sucesso", "Relatório salvo com sucesso em: " + nomeArquivo);
            } catch (IOException e) {
                userInterface.showError("Erro ao salvar o arquivo: " + e.getMessage());
            }
        }
    }
}