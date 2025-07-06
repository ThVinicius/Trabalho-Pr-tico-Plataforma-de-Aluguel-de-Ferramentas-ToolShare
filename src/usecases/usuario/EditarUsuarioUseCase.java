package usecases.usuario;

import exceptions.FormatoDadosException;
import exceptions.ValidacaoException;
import interfaces.IUserInterface;
import models.Usuario;
import utils.InputHandler;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import static config.Config.DATE_FORMATTER;

public class EditarUsuarioUseCase {
    private final IUserInterface userInterface;
    private final InputHandler inputHandler;

    public EditarUsuarioUseCase(IUserInterface userInterface) {
        this.userInterface = userInterface;
        this.inputHandler = new InputHandler(userInterface);
    }

    public void execute(List<Usuario> usuarios) throws FormatoDadosException, ValidacaoException {
        String title = "Editar Cadastro";

        String cpf = this.inputHandler.notEmpty(title, "Digite o CPF do usuário a editar:");
        if (cpf == null) return;

        if (!Usuario.validarCPF(cpf)) {
            throw new FormatoDadosException("Erro: CPF inválido!");
        }

        Usuario usuario = usuarios.stream()
                .filter(u -> u.getCpf().equals(cpf))
                .findFirst()
                .orElseThrow(() -> new ValidacaoException("Erro: Usuário não encontrado!"));
        
        String mensagemAtual = "Dados atuais:\n" +
                "Nome: " + usuario.getNome() + "\n" +
                "Contato: " + usuario.getContato() + "\n" +
                "CPF: " + usuario.getCpf();
        this.userInterface.showMessage("Dados do Usuário", mensagemAtual);

        String novoNome = this.inputHandler.notEmpty(title, "Novo nome:");
        if (novoNome == null) return;
        String novoContato = this.inputHandler.notEmpty(title, "Novo contato:");
        if (novoContato == null) return;
        LocalDate novaDataNascimento = this.handleDataNascInput(title, "Nova data de nascimento (DD/MM/AAAA):");
        if (novaDataNascimento == null) return;


        usuario.setNome(novoNome);
        usuario.setContato(novoContato);
        usuario.setDataNascimento(novaDataNascimento);
        this.userInterface.showMessage("Sucesso", "Cadastro editado com sucesso!");
    }

    private LocalDate handleDataNascInput(String title, String message) throws FormatoDadosException, ValidacaoException {
        String dataNascimentoStr = this.inputHandler.notEmpty(title, message);
        if (dataNascimentoStr == null) return null;

        try {
            LocalDate dataNascimento = LocalDate.parse(dataNascimentoStr, DATE_FORMATTER);
            if (dataNascimento.isAfter(LocalDate.now())) {
                throw new ValidacaoException("Erro: Data de nascimento não pode ser futura!");
            }
            return dataNascimento;
        } catch (DateTimeParseException e) {
            throw new FormatoDadosException("Erro: Formato de data inválido! Use DD/MM/AAAA.");
        }
    }
}