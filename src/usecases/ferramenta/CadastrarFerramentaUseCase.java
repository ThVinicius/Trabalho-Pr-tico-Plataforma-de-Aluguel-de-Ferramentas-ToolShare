package usecases.ferramenta;

import enums.StatusFerramenta;
import interfaces.IUserInterface;
import models.*;
import utils.InputHandler;

import java.util.List;

public class CadastrarFerramentaUseCase {
    private final IUserInterface userInterface;
    private final InputHandler inputHandler;

    public CadastrarFerramentaUseCase(IUserInterface userInterface) {
        this.userInterface = userInterface;
        this.inputHandler = new InputHandler(userInterface);
    }

    public void execute(List<Usuario> usuarios, List<Ferramenta> ferramentas) {
        String title = "Cadastrar Nova Ferramenta";

        String cpf = this.inputHandler.notEmpty(title, "Digite o CPF do proprietário:");
        if (cpf == null) return;
        Usuario proprietario = usuarios.stream().filter(u -> u.getCpf().equals(cpf)).findFirst().orElse(null);

        if (proprietario == null) {
            this.userInterface.showError("Usuário não encontrado!");
            return;
        }

        if (!(proprietario instanceof Locador locador)) {
            this.userInterface.showError("Apenas usuários do tipo 'Locador' podem cadastrar ferramentas.");
            return;
        }

        long contadorFerramentas = ferramentas.stream().filter(f -> f.getProprietario().equals(locador)).count();
        if (contadorFerramentas >= 5) {
            this.userInterface.showError("Limite máximo de 5 ferramentas atingido!");
            return;
        }

        String nome = this.inputHandler.notEmpty(title, "Nome da ferramenta:");
        if (nome == null) return;
        String descricao = this.inputHandler.notEmpty(title, "Descrição da ferramenta:");
        if (descricao == null) return;

        String[] categorias = {"Elétrica", "Manual", "Jardim"};
        String categoria = this.userInterface.getInput(title, "Selecione a categoria:", categorias);
        if (categoria == null) return;

        double precoPorDia = this.inputHandler.getDouble(title, "Preço por dia:");
        
        double precoMinimo = switch (categoria) {
            case "Elétrica" -> 15.0;
            case "Manual" -> 8.0;
            case "Jardim" -> 12.0;
            default -> 0.0;
        };
        if (precoPorDia < precoMinimo) {
            this.userInterface.showError("Preço abaixo do mínimo (R$" + precoMinimo + ") para a categoria " + categoria + "!");
            return;
        }

        Ferramenta novaFerramenta = null;
        switch (categoria) {
            case "Elétrica" -> {
                int voltagem = this.inputHandler.getInt(title, "Voltagem da ferramenta (V):");
                double potencia = this.inputHandler.getDouble(title, "Potência da ferramenta (W):");
                novaFerramenta = new FerramentaEletrica(nome, descricao, precoPorDia, StatusFerramenta.DISPONIVEL, locador, voltagem, potencia);
            }
            case "Manual" -> {
                double peso = this.inputHandler.getDouble(title, "Peso da ferramenta (kg):");
                String material = this.inputHandler.notEmpty(title, "Material da ferramenta:");
                novaFerramenta = new FerramentaManual(nome, descricao, precoPorDia, StatusFerramenta.DISPONIVEL, locador, peso, material);
            }
            case "Jardim" -> {
                String tipoCorte = this.inputHandler.notEmpty(title, "Tipo de corte:");
                int nivelSeguranca = this.inputHandler.getInt(title, "Nível de segurança (1 a 5):");
                novaFerramenta = new FerramentaJardim(nome, descricao, precoPorDia, StatusFerramenta.DISPONIVEL, locador, tipoCorte, nivelSeguranca);
            }
        }

        if (novaFerramenta != null) {
            ferramentas.add(novaFerramenta);
            locador.setQuantidadeFerramentas((int) (contadorFerramentas + 1));
            this.userInterface.showMessage("Sucesso", "Ferramenta cadastrada com sucesso! Código: " + novaFerramenta.getCodigo());
        }
    }
}