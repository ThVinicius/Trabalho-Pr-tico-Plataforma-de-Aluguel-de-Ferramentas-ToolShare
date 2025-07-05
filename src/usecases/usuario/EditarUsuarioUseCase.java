package usecases.usuario;

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

    public void execute(List<Usuario> usuarios) {
        String title = "Editar Cadastro";

        String cpf = this.inputHandler.notEmpty(title, "Digite o CPF do usuário a editar:");
        if (cpf == null) return;

        if (!Usuario.validarCPF(cpf)) {
            this.userInterface.showError("Erro: CPF inválido!");
            return;
        }

        Usuario usuario = null;
        for (Usuario u : usuarios) {
            if (u.getCpf().equals(cpf)) {
                usuario = u;
                break;
            }
        }

        if (usuario == null) {
            this.userInterface.showError("Erro: Usuário não encontrado!");
            return;
        }

        // Exibe dados atuais
        String mensagemAtual = "Dados atuais:\n" +
                "Nome: " + usuario.getNome() + "\n" +
                "Contato: " + usuario.getContato() + "\n" +
                "CPF: " + usuario.getCpf();
        this.userInterface.showMessage("Dados do Usuário", mensagemAtual);

        String novoNome = this.inputHandler.notEmpty(title, "Novo nome:");
        if (novoNome == null) return;
        String novoContato = this.inputHandler.notEmpty(title, "Novo contato:");
        if (novoContato == null) return;
        LocalDate novaDataNascimentoStr = this.handleDataNascInput(title, "Nova data de nascimento (DD/MM/AAAA):");
        if (novaDataNascimentoStr == null) return;

        // Atualiza os dados
        usuario.setNome(novoNome);
        usuario.setContato(novoContato);
        usuario.setDataNascimento(novaDataNascimentoStr);
        this.userInterface.showMessage("Sucesso", "Cadastro editado com sucesso!");
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
}