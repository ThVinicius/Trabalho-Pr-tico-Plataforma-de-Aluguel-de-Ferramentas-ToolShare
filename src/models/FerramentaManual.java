package models;

import enums.StatusFerramenta;

public class FerramentaManual extends Ferramenta {
    private double peso;
    private String material;

    public FerramentaManual(String nome, String descricao, double precoPorDia, StatusFerramenta status, Usuario proprietario, double peso, String material) {
        super(nome, descricao, precoPorDia, status, proprietario);
        this.peso = peso;
        this.material = material;
    }

    @Override
    public double calcularValorAluguel(int dias) {
        double valorTotal = getPrecoPorDia() * dias;
        if (this.peso > 5) {
            valorTotal += (5.0 * dias);
        }
        return valorTotal;
    }

    @Override
    public String getNomeCategoria() {
        return "Manual";
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}