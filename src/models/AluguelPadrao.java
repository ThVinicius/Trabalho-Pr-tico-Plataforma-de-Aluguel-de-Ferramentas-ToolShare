package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class AluguelPadrao extends Transacao implements Serializable {
    private static final long serialVersionUID = 1L;

    public AluguelPadrao(Usuario usuario, Ferramenta ferramenta, LocalDateTime dataInicio, int periodo) {
        super(usuario, ferramenta, dataInicio, periodo);
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
        return ferramenta.calcularValorAluguel(getPeriodo()) + getMulta();
    }
}