package models;

import enums.StatusFerramenta;

import java.io.Serializable;
import java.util.List;

public abstract class Ferramenta implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int proximoCodigo = 1;
    private final int codigo;
    private String nome;
    private String descricao;
    private double precoPorDia;
    private StatusFerramenta status; // MUDANÇA: Usando o enum
    private Usuario proprietario;

    public Ferramenta(String nome, String descricao, double precoPorDia, StatusFerramenta status, Usuario proprietario) {
        this.codigo = proximoCodigo++;
        this.nome = nome;
        this.descricao = descricao;
        this.precoPorDia = precoPorDia;
        this.status = status;
        this.proprietario = proprietario;
    }

    public static void atualizarProximoCodigo(List<Ferramenta> ferramentas) {
        if (!ferramentas.isEmpty()) {
            proximoCodigo = ferramentas.stream()
                    .mapToInt(Ferramenta::getCodigo)
                    .max()
                    .orElse(0) + 1;
        }
    }

    public abstract double calcularValorAluguel(int dias);

    public abstract String getNomeCategoria();

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPrecoPorDia() {
        return precoPorDia;
    }

    public void setPrecoPorDia(double precoPorDia) {
        this.precoPorDia = precoPorDia;
    }

    public StatusFerramenta getStatus() {
        return status;
    }

    public void setStatus(StatusFerramenta status) {
        this.status = status;
    }

    public Usuario getProprietario() {
        return proprietario;
    }

    public void setProprietario(Usuario proprietario) {
        this.proprietario = proprietario;
    }
}