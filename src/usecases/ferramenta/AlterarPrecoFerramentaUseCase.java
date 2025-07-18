package usecases.ferramenta;

import exceptions.FormatoDadosException;
import exceptions.ValidacaoException;
import interfaces.IUserInterface;
import models.Ferramenta;
import utils.InputHandler;

import java.util.List;

public class AlterarPrecoFerramentaUseCase {
    private final IUserInterface userInterface;
    private final InputHandler inputHandler;

    public AlterarPrecoFerramentaUseCase(IUserInterface userInterface) {
        this.userInterface = userInterface;
        this.inputHandler = new InputHandler(userInterface);
    }

    public void execute(List<Ferramenta> ferramentas) throws FormatoDadosException, ValidacaoException {
        Integer codigo = this.inputHandler.getInt("Alterar Preço", "Digite o código da ferramenta:");
        if (codigo == null) return;

        Ferramenta ferramenta = ferramentas.stream()
                .filter(f -> f.getCodigo() == codigo)
                .findFirst()
                .orElseThrow(() -> new ValidacaoException("Erro: Ferramenta não encontrada!"));

        Double novoPreco = this.inputHandler.getDouble("Alterar Preço", "Novo preço por dia:");
        if (novoPreco == null) return;

        String categoria = ferramenta.getNomeCategoria();
        double precoMinimo = switch (categoria) {
            case "Elétrica" -> 15.0;
            case "Manual" -> 8.0;
            case "Jardim" -> 12.0;
            default -> 0.0;
        };

        if (novoPreco < precoMinimo) {
            throw new ValidacaoException("Erro: Preço abaixo do mínimo (R$" + String.format("%.2f", precoMinimo) + ") para a categoria " + categoria + "!");
        }

        ferramenta.setPrecoPorDia(novoPreco);
        this.userInterface.showMessage("Sucesso", "Preço alterado com sucesso!");
    }
}