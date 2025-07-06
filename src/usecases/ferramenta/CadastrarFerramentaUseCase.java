package usecases.ferramenta;

import enums.StatusFerramenta;
import exceptions.FormatoDadosException;
import exceptions.ValidacaoException;
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

    public void execute(List<Usuario> usuarios, List<Ferramenta> ferramentas) throws ValidacaoException, FormatoDadosException {
        String title = "Cadastrar Nova Ferramenta";

        String cpf = this.inputHandler.notEmpty(title, "Digite o CPF do proprietário:");
        if (cpf == null) return;
        Usuario proprietario = usuarios.stream().filter(u -> u.getCpf().equals(cpf)).findFirst().orElse(null);

        if (proprietario == null) {
            throw new ValidacaoException("Usuário proprietário não encontrado!");
        }

        if (!(proprietario instanceof Locador locador)) {
            throw new ValidacaoException("Apenas usuários do tipo 'Locador' podem cadastrar ferramentas.");
        }

        long contadorFerramentas = ferramentas.stream().filter(f -> f.getProprietario().equals(locador)).count();
        if (contadorFerramentas >= 5) {
            throw new ValidacaoException("Limite máximo de 5 ferramentas por locador foi atingido!");
        }

        String[] categorias = {"Elétrica", "Manual", "Jardim"};
        String categoria = this.userInterface.getInput(title, "Selecione a categoria:", categorias);
        if (categoria == null) return;

        String nome = this.inputHandler.notEmpty(title, "Nome da ferramenta:");
        if (nome == null) return;
        String descricao = this.inputHandler.notEmpty(title, "Descrição da ferramenta:");
        if (descricao == null) return;

        double precoPorDia = this.inputHandler.getDouble(title, "Preço por dia:");

        double precoMinimo = switch (categoria) {
            case "Elétrica" -> 15.0;
            case "Manual" -> 8.0;
            case "Jardim" -> 12.0;
            default -> 0.0;
        };
        if (precoPorDia < precoMinimo) {
            throw new ValidacaoException("Preço abaixo do mínimo (R$" + precoMinimo + ") para a categoria " + categoria + "!");
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
                if (peso <= 0) {
                    throw new ValidacaoException("O peso da ferramenta manual deve ser um valor positivo.");
                }
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