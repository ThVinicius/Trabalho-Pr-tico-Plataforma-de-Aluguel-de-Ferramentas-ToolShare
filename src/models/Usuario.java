package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

public abstract class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private String cpf;
    private String contato;
    private LocalDate dataNascimento;

    public Usuario(String nome, String cpf, String contato, LocalDate dataNascimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.contato = contato;
        this.dataNascimento = dataNascimento;
    }

    public static boolean validarCPF(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }
        return cpf.length() == 11;
    }

    public abstract boolean validarIdentificacao();

    public int calcularIdade() {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}