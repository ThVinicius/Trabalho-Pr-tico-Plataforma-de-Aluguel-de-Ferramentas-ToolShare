package usecases.usuario;

import interfaces.IUserInterface;
import models.Locador;
import models.Locatario;
import models.Usuario;
import utils.InputHandler;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import static config.Config.DATE_FORMATTER;

public class CadastrarUsuarioUseCase {
    private final IUserInterface userInterface;
    private final InputHandler inputHandler;

    public CadastrarUsuarioUseCase(IUserInterface userInterface) {
        this.userInterface = userInterface;
        this.inputHandler = new InputHandler(userInterface);
    }

    public void execute(List<Usuario> usuarios) {
        String inputTitle = "Cadastro de Novo Usuário";

        String tipoUsuarioStr = this.userInterface.getInput(inputTitle, "Qual tipo de perfil deseja cadastrar?", new String[]{"Locador", "Locatário"});
        if (tipoUsuarioStr == null) return;

        String cpf = this.handleCpfInput(inputTitle, "CPF:", usuarios);
        if (cpf == null) return;
        String nome = this.inputHandler.notEmpty(inputTitle, "Nome:");
        if (nome == null) return;
        String contato = this.inputHandler.notEmpty(inputTitle, "Contato (e-mail ou telefone):");
        if (contato == null) return;
        LocalDate dataNascimento = this.handleDataNascInput(inputTitle, "Data de nascimento (DD/MM/AAAA):");
        if (dataNascimento == null) return;

        Usuario novoUsuario = null;

        if ("Locador".equalsIgnoreCase(tipoUsuarioStr)) {
            novoUsuario = new Locador(nome, cpf, contato, dataNascimento, 5.0);
        } else if ("Locatário".equalsIgnoreCase(tipoUsuarioStr)) {
            Double limiteCredito = this.handleLimiteCredito(inputTitle);
            if (limiteCredito == null) return;
            novoUsuario = new Locatario(nome, cpf, contato, dataNascimento, limiteCredito);
        }

        if (novoUsuario != null) {
            usuarios.add(novoUsuario);
            this.userInterface.showMessage("Sucesso", "Usuário (" + tipoUsuarioStr + ") cadastrado com sucesso!");
        } else {
            this.userInterface.showError("Tipo de usuário inválido. Operação cancelada.");
        }
    }

    private String handleCpfInput(String title, String message, List<Usuario> usuarios) {
        String cpf = this.inputHandler.notEmpty(title, "CPF:");
        if (cpf == null) return null;

        if (!Usuario.validarCPF(cpf)) {
            this.userInterface.showMessage("Erro", "Erro: CPF inválido!");
            return handleCpfInput(title, message, usuarios);
        }

        for (Usuario usuario : usuarios) {
            if (usuario.getCpf().equals(cpf)) {
                this.userInterface.showError("Erro: CPF já cadastrado no sistema!");
                return handleCpfInput(title, message, usuarios);
            }
        }

        return cpf;
    }

    private LocalDate handleDataNascInput(String title, String message) {
        String dataNascimentoStr = this.inputHandler.notEmpty(title, "Data de nascimento (DD/MM/AAAA):");
        if (dataNascimentoStr == null) return null;

        LocalDate dataNascimento;
        try {
            dataNascimento = LocalDate.parse(dataNascimentoStr, DATE_FORMATTER);
            if (dataNascimento.isAfter(LocalDate.now())) {
                this.userInterface.showError("Erro: Data de nascimento não pode ser futura!");
                return this.handleDataNascInput(title, message);
            }
        } catch (DateTimeParseException e) {
            this.userInterface.showError("Erro: Formato de data inválido! Use DD/MM/AAAA.");
            return this.handleDataNascInput(title, message);
        }

        return dataNascimento;
    }

    private Double handleLimiteCredito(String inputTitle) {
        String limiteCreditoStr = this.inputHandler.notEmpty(inputTitle, "Informe o limite de crédito inicial:");
        if (limiteCreditoStr == null) return null;

        Double limiteCredito;
        try {
            limiteCredito = Double.parseDouble(limiteCreditoStr);

            if (limiteCredito < 0) {
                this.userInterface.showError("Erro: O limite de crédito não pode ser negativo!");
                return this.handleLimiteCredito(inputTitle);
            }

        } catch (NumberFormatException e) {
            this.userInterface.showError("Erro: Valor inválido! Informe um número válido para o limite de crédito.");
            return this.handleLimiteCredito(inputTitle);
        }

        return limiteCredito;
    }
}