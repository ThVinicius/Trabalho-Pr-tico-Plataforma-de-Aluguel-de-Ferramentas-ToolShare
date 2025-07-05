package models;

import java.io.Serializable;
import java.time.LocalDate;

public class Locador extends Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private int quantidadeFerramentas;
    private double avaliacao;

    public Locador(String nome, String cpf, String contato, LocalDate dataNascimento, double avaliacaoInicial) {
        super(nome, cpf, contato, dataNascimento);
        this.quantidadeFerramentas = 0;
        this.avaliacao = avaliacaoInicial;
    }

    @Override
    public boolean validarIdentificacao() {
        return this.quantidadeFerramentas >= 1 && this.avaliacao >= 3.0;
    }

    public int getQuantidadeFerramentas() {
        return quantidadeFerramentas;
    }

    public void setQuantidadeFerramentas(int quantidadeFerramentas) {
        this.quantidadeFerramentas = quantidadeFerramentas;
    }

    public double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(double avaliacao) {
        this.avaliacao = avaliacao;
    }
}