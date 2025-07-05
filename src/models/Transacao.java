package models;

import java.time.LocalDateTime;

public abstract class Transacao {
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