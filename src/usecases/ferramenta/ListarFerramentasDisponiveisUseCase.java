package usecases.ferramenta;

import enums.StatusFerramenta;
import interfaces.IUserInterface;
import models.Ferramenta;

import java.util.ArrayList;
import java.util.List;

public class ListarFerramentasDisponiveisUseCase {
    private final IUserInterface userInterface;

    public ListarFerramentasDisponiveisUseCase(IUserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void execute(List<Ferramenta> ferramentas) {
        List<Ferramenta> disponiveis = new ArrayList<>();
        for (Ferramenta f : ferramentas) {
            if (f.getStatus() == StatusFerramenta.DISPONIVEL) {
                disponiveis.add(f);
            }
        }

        if (disponiveis.isEmpty()) {
            this.userInterface.showMessage("Ferramentas Disponíveis", "Nenhuma ferramenta disponível!");
            return;
        }

        StringBuilder mensagem = new StringBuilder("Ferramentas Disponíveis:\n\n");
        for (Ferramenta f : disponiveis) {
            mensagem.append("Código: ").append(f.getCodigo()).append("\n")
                    .append("Nome: ").append(f.getNome()).append("\n")
                    .append("Descrição: ").append(f.getDescricao()).append("\n")
                    .append("Preço por dia: R$").append(String.format("%.2f", f.getPrecoPorDia())).append("\n")
                    .append("Categoria: ").append(f.getNomeCategoria()).append("\n")
                    .append("Proprietário: ").append(f.getProprietario().getNome()).append(" (CPF: ").append(f.getProprietario().getCpf()).append(")\n")
                    .append("------------------------\n");
        }
        this.userInterface.showMessage("Ferramentas Disponíveis", mensagem.toString());
    }
}