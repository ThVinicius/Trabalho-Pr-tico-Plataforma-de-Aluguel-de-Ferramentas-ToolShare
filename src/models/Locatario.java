package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Locatario extends Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Transacao> historicoAlugueis;
    private double limiteCredito;

    public Locatario(String nome, String cpf, String contato, LocalDate dataNascimento, double limiteCredito) {
        super(nome, cpf, contato, dataNascimento);
        this.limiteCredito = limiteCredito;
        this.historicoAlugueis = new ArrayList<>();
    }

    @Override
    public boolean validarIdentificacao() {
        return this.limiteCredito > 0 && this.historicoAlugueis.size() < 10;
    }

    public List<Transacao> getHistoricoAlugueis() {
        return historicoAlugueis;
    }

    public void setHistoricoAlugueis(List<Transacao> historicoAlugueis) {
        this.historicoAlugueis = historicoAlugueis;
    }

    public double getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(double limiteCredito) {
        this.limiteCredito = limiteCredito;
    }
}