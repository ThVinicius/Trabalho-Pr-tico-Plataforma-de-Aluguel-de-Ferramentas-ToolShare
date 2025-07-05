package models;

import enums.StatusFerramenta;

public class FerramentaJardim extends Ferramenta {
    private String tipoCorte;
    private int nivelSeguranca;

    public FerramentaJardim(String nome, String descricao, double precoPorDia, StatusFerramenta status, Usuario proprietario, String tipoCorte, int nivelSeguranca) {
        super(nome, descricao, precoPorDia, status, proprietario);
        this.tipoCorte = tipoCorte;
        this.nivelSeguranca = nivelSeguranca;
    }

    @Override
    public double calcularValorAluguel(int dias) {
        return getPrecoPorDia() * dias;
    }

    @Override
    public String getNomeCategoria() {
        return "Jardim";
    }

    public String getTipoCorte() {
        return tipoCorte;
    }

    public void setTipoCorte(String tipoCorte) {
        this.tipoCorte = tipoCorte;
    }

    public int getNivelSeguranca() {
        return nivelSeguranca;
    }

    public void setNivelSeguranca(int nivelSeguranca) {
        this.nivelSeguranca = nivelSeguranca;
    }
}