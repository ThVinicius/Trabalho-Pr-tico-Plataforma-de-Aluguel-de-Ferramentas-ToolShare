package usecases.aluguel;

import enums.StatusFerramenta;
import exceptions.FerramentaIndisponivelException;
import exceptions.FormatoDadosException;
import exceptions.SaldoInsuficienteException;
import exceptions.ValidacaoException;
import interfaces.IUserInterface;
import models.*;
import utils.InputHandler;

import java.time.LocalDateTime;
import java.util.List;

public class RealizarAluguelUseCase {
    private final IUserInterface userInterface;
    private final InputHandler inputHandler;

    public RealizarAluguelUseCase(IUserInterface userInterface) {
        this.userInterface = userInterface;
        this.inputHandler = new InputHandler(userInterface);
    }

    public void execute(List<Usuario> usuarios, List<Ferramenta> ferramentas, List<Transacao> transacoes) throws ValidacaoException, FerramentaIndisponivelException, SaldoInsuficienteException, FormatoDadosException {
        String title = "Realizar Aluguel";

        String cpf = this.inputHandler.notEmpty(title, "Digite o CPF do usuário:");
        if (cpf == null) return;
        Usuario usuario = usuarios.stream().filter(u -> u.getCpf().equals(cpf)).findFirst().orElse(null);

        if (usuario == null) throw new ValidacaoException("Usuário não encontrado!");
        if (!(usuario instanceof Locatario locatario))
            throw new ValidacaoException("Apenas 'Locatários' podem alugar.");
        if (!locatario.validarIdentificacao())
            throw new SaldoInsuficienteException("Aluguel bloqueado: Limite de crédito ou de aluguéis ativos atingido.");

        int codigo = this.inputHandler.getInt(title, "Digite o código da ferramenta:");
        Ferramenta ferramenta = ferramentas.stream().filter(f -> f.getCodigo() == codigo).findFirst().orElse(null);
        if (ferramenta == null) throw new ValidacaoException("Ferramenta não encontrada!");

        if (ferramenta.getStatus() != StatusFerramenta.DISPONIVEL)
            throw new FerramentaIndisponivelException("Ferramenta não está disponível!");
        if (ferramenta.getProprietario().equals(locatario))
            throw new ValidacaoException("Usuário não pode alugar a própria ferramenta!");
        if (ferramenta instanceof FerramentaEletrica && locatario.calcularIdade() < 18)
            throw new ValidacaoException("Menores de 18 anos não podem alugar ferramentas elétricas!");

        int periodo = this.inputHandler.getInt(title, "Digite o período de aluguel (em dias):");
        if (periodo <= 0) throw new ValidacaoException("Período de aluguel deve ser maior que zero!");
        if (ferramenta instanceof FerramentaJardim && periodo < 2)
            throw new ValidacaoException("Ferramentas de jardim requerem aluguel mínimo de 2 dias!");
        if (ferramenta instanceof FerramentaJardim fj && fj.getNivelSeguranca() < 3 && periodo > 3)
            throw new ValidacaoException("Ferramentas de jardim com baixa segurança não podem ser alugadas por mais de 3 dias.");

        double valorTotal = ferramenta.calcularValorAluguel(periodo);
        String confirm = this.userInterface.getInput(title, "O valor total do aluguel é R$ " + String.format("%.2f", valorTotal) + ". Confirmar?", new String[]{"Sim", "Não"});
        if (confirm == null) return;

        if (!"Sim".equalsIgnoreCase(confirm)) {
            this.userInterface.showMessage(title, "Operação cancelada.");
            return;
        }

        Transacao novaTransacao;
        String aplicarDesconto = this.userInterface.getInput(title, "Deseja aplicar um desconto promocional?", new String[]{"Sim", "Não"});
        if (aplicarDesconto == null) return;

        if ("Sim".equalsIgnoreCase(aplicarDesconto)) {
            Double descontoPercentual = this.inputHandler.getDouble(title, "Digite o percentual de desconto (ex: 10 para 10%):");
            if (descontoPercentual == null) return;

            if (descontoPercentual <= 0 || descontoPercentual >= 100) {
                this.userInterface.showError("Percentual de desconto inválido. Deve ser maior que 0 e menor que 100.");
                return;
            }
            double descontoDecimal = descontoPercentual / 100.0;
            novaTransacao = new AluguelPromocional(locatario, ferramenta, LocalDateTime.now(), periodo, descontoDecimal);
            this.userInterface.showMessage("Sucesso", "Desconto de " + descontoPercentual + "% aplicado!");

        } else {
            novaTransacao = new AluguelPadrao(locatario, ferramenta, LocalDateTime.now(), periodo);
        }

        transacoes.add(novaTransacao);
        locatario.getHistoricoAlugueis().add(novaTransacao);
        ferramenta.setStatus(StatusFerramenta.ALUGADA);

        this.userInterface.showMessage("Sucesso", "Aluguel realizado com sucesso! ID da transação: " + novaTransacao.getId());
    }
}