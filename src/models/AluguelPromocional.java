package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class AluguelPromocional extends Transacao implements Serializable {
    private static final long serialVersionUID = 1L;
    private final double desconto;

    public AluguelPromocional(Usuario usuario, Ferramenta ferramenta, LocalDateTime dataInicio, int periodo, double desconto) {
        super(usuario, ferramenta, dataInicio, periodo);
        this.desconto = desconto;
    }

    @Override
    public double calcularMulta() {
        if (dataDevolucao == null || !dataDevolucao.isAfter(dataFim)) {
            return 0.0;
        }
        long diasAtraso = ChronoUnit.DAYS.between(dataFim, dataDevolucao);
        return ferramenta.getPrecoPorDia() * 0.1 * diasAtraso;
    }

    @Override
    protected double calcularCustoFinal() {
        double valorBase = ferramenta.calcularValorAluguel(getPeriodo());
        double valorComDesconto = valorBase * (1 - this.desconto);
        return valorComDesconto + getMulta();
    }
}