package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public abstract class Transacao implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int proximoId = 1;
    protected int id;
    protected Usuario usuario;
    protected Ferramenta ferramenta;
    protected LocalDateTime dataInicio;
    protected int periodo;
    protected LocalDateTime dataFim;
    protected LocalDateTime dataDevolucao;
    protected double custoTotal;
    protected double multa;

    public Transacao(Usuario usuario, Ferramenta ferramenta, LocalDateTime dataInicio, int periodo) {
        this.id = proximoId++;
        this.usuario = usuario;
        this.ferramenta = ferramenta;
        this.dataInicio = dataInicio;
        this.periodo = periodo;
        this.dataFim = dataInicio.plusDays(periodo);
    }

    public static void atualizarProximoId(List<Transacao> transacoes) {
        if (!transacoes.isEmpty()) {
            proximoId = transacoes.stream()
                    .mapToInt(Transacao::getId)
                    .max()
                    .orElse(0) + 1;
        }
    }

    public abstract double calcularMulta();

    protected abstract double calcularCustoFinal();

    public void registrarDevolucao(LocalDateTime dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
        this.multa = this.calcularMulta();
        this.custoTotal = this.calcularCustoFinal();
    }

    public int getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Ferramenta getFerramenta() {
        return ferramenta;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public int getPeriodo() {
        return periodo;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public LocalDateTime getDataDevolucao() {
        return dataDevolucao;
    }

    public double getCustoTotal() {
        return custoTotal;
    }

    public double getMulta() {
        return multa;
    }
}