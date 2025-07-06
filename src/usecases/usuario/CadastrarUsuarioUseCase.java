package usecases.usuario;

import exceptions.FormatoDadosException;
import exceptions.ValidacaoException;
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

    public void execute(List<Usuario> usuarios) throws FormatoDadosException, ValidacaoException {
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

    private String handleCpfInput(String title, String message, List<Usuario> usuarios) throws FormatoDadosException, ValidacaoException {
        String cpf = this.inputHandler.notEmpty(title, "CPF:");
        if (cpf == null) return null;

        if (!Usuario.validarCPF(cpf)) {
            throw new FormatoDadosException("Formato de CPF inválido! Deve conter 11 dígitos.");
        }
        for (Usuario usuario : usuarios) {
            if (usuario.getCpf().equals(cpf)) {
                throw new ValidacaoException("Erro: CPF já cadastrado no sistema!");
            }
        }
        return cpf;
    }

    private LocalDate handleDataNascInput(String title, String message) throws FormatoDadosException, ValidacaoException {
        String dataNascimentoStr = this.inputHandler.notEmpty(title, message);
        if (dataNascimentoStr == null) return null;
        try {
            LocalDate dataNascimento = LocalDate.parse(dataNascimentoStr, DATE_FORMATTER);
            if (dataNascimento.isAfter(LocalDate.now())) {
                throw new ValidacaoException("Data de nascimento não pode ser futura!");
            }
            return dataNascimento;
        } catch (DateTimeParseException e) {
            throw new FormatoDadosException("Formato de data inválido! Use DD/MM/AAAA.");
        }
    }

    private Double handleLimiteCredito(String inputTitle) throws FormatoDadosException, ValidacaoException {
        Double limiteCredito = this.inputHandler.getDouble(inputTitle, "Informe o limite de crédito inicial:");
        if (limiteCredito == null) return null;
        if (limiteCredito < 0) {
            throw new ValidacaoException("O limite de crédito não pode ser negativo!");
        }
        return limiteCredito;
    }
}