package usecases.ferramenta;

import exceptions.FormatoDadosException;
import interfaces.IUserInterface;
import models.Ferramenta;
import utils.InputHandler;

import java.util.ArrayList;
import java.util.List;

public class BuscarFerramentaUseCase {
    private final IUserInterface userInterface;
    private final InputHandler inputHandler;

    public BuscarFerramentaUseCase(IUserInterface userInterface) {
        this.userInterface = userInterface;
        this.inputHandler = new InputHandler(userInterface);
    }

    public void execute(List<Ferramenta> ferramentas) throws FormatoDadosException {
        String busca = this.inputHandler.notEmpty("Buscar Ferramenta", "Digite o nome ou código da ferramenta:");
        if (busca == null) return;

        List<Ferramenta> resultados = new ArrayList<>();
        try {
            int codigo = Integer.parseInt(busca);
            for (Ferramenta f : ferramentas) {
                if (f.getCodigo() == codigo) {
                    resultados.add(f);
                    break;
                }
            }
        } catch (NumberFormatException e) {
            for (Ferramenta f : ferramentas) {
                if (f.getNome().toLowerCase().contains(busca.toLowerCase())) {
                    resultados.add(f);
                }
            }
        }

        if (resultados.isEmpty()) {
            this.userInterface.showMessage("Resultado da Busca", "Nenhuma ferramenta encontrada!");
            return;
        }

        StringBuilder mensagem = new StringBuilder("Ferramentas encontradas:\n\n");
        for (Ferramenta f : resultados) {
            mensagem.append("Código: ").append(f.getCodigo()).append("\n")
                    .append("Nome: ").append(f.getNome()).append("\n")
                    .append("Descrição: ").append(f.getDescricao()).append("\n")
                    .append("Preço por dia: R$").append(f.getPrecoPorDia()).append("\n")
                    .append("Categoria: ").append(f.getNomeCategoria()).append("\n")
                    .append("Status: ").append(f.getStatus()).append("\n")
                    .append("Proprietário: ").append(f.getProprietario().getNome()).append(" (CPF: ").append(f.getProprietario().getCpf()).append(")\n")
                    .append("------------------------\n");
        }
        this.userInterface.showMessage("Resultado da Busca", mensagem.toString());
    }
}