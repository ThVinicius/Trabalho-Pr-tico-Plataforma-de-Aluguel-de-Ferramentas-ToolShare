package models;

import enums.StatusFerramenta;

import java.io.Serializable;

public class FerramentaEletrica extends Ferramenta implements Serializable {
    private static final long serialVersionUID = 1L;
    private int voltagem;
    private double potencia;

    public FerramentaEletrica(String nome, String descricao, double precoPorDia, StatusFerramenta status, Usuario proprietario, int voltagem, double potencia) {
        super(nome, descricao, precoPorDia, status, proprietario);
        this.voltagem = voltagem;
        this.potencia = potencia;
    }

    @Override
    public double calcularValorAluguel(int dias) {
        double valorBase = getPrecoPorDia() * dias;
        if (dias > 5) {
            return valorBase * 0.90;
        }
        return valorBase;
    }

    @Override
    public String getNomeCategoria() {
        return "Elétrica";
    }

    public int getVoltagem() {
        return voltagem;
    }

    public void setVoltagem(int voltagem) {
        this.voltagem = voltagem;
    }

    public double getPotencia() {
        return potencia;
    }

    public void setPotencia(double potencia) {
        this.potencia = potencia;
    }
}