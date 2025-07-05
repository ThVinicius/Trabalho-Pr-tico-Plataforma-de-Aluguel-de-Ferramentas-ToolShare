package models;

import enums.MenuAluguel;
import enums.MenuFerramenta;
import enums.MenuType;
import enums.MenuUsuario;
import interfaces.IUserInterface;
import usecases.aluguel.*;
import usecases.ferramenta.*;
import usecases.usuario.*;

import java.util.ArrayList;
import java.util.List;

public class Plataforma {
    private final IUserInterface userInterface;

    private final CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    private final ConsultarUsuarioPorCpfUseCase consultarUsuarioPorCpfUseCase;
    private final ListarUsuariosUseCase listarUsuariosUseCase;
    private final EditarUsuarioUseCase editarUsuarioUseCase;
    private final ExcluirUsuarioUseCase excluirUsuarioUseCase;
    private final CadastrarFerramentaUseCase cadastrarFerramentaUseCase;
    private final BuscarFerramentaUseCase buscarFerramentaUseCase;
    private final AlterarPrecoFerramentaUseCase alterarPrecoFerramentaUseCase;
    private final AtualizarStatusFerramentaUseCase atualizarStatusFerramentaUseCase;
    private final ListarFerramentasDisponiveisUseCase listarFerramentasDisponiveisUseCase;
    private final RealizarAluguelUseCase realizarAluguelUseCase;
    private final RegistrarDevolucaoUseCase registrarDevolucaoUseCase;
    private final ConsultarAluguelAtivoUseCase consultarAluguelAtivoUseCase;
    private final CalcularMultaPorAtrasoUseCase calcularMultaPorAtrasoUseCase;
    private final ListarHistoricoAlugueisUseCase listarHistoricoAlugueisUseCase;

    private final List<Ferramenta> ferramentas = new ArrayList<>();
    private final List<Transacao> transacoes = new ArrayList<>();
    private final List<Usuario> usuarios = new ArrayList<>();

    public Plataforma(IUserInterface userInterface) {
        this.userInterface = userInterface;
        this.cadastrarUsuarioUseCase = new CadastrarUsuarioUseCase(userInterface);
        this.consultarUsuarioPorCpfUseCase = new ConsultarUsuarioPorCpfUseCase(userInterface);
        this.listarUsuariosUseCase = new ListarUsuariosUseCase(userInterface);
        this.editarUsuarioUseCase = new EditarUsuarioUseCase(userInterface);
        this.excluirUsuarioUseCase = new ExcluirUsuarioUseCase(userInterface);
        this.cadastrarFerramentaUseCase = new CadastrarFerramentaUseCase(userInterface);
        this.buscarFerramentaUseCase = new BuscarFerramentaUseCase(userInterface);
        this.alterarPrecoFerramentaUseCase = new AlterarPrecoFerramentaUseCase(userInterface);
        this.atualizarStatusFerramentaUseCase = new AtualizarStatusFerramentaUseCase(userInterface);
        this.listarFerramentasDisponiveisUseCase = new ListarFerramentasDisponiveisUseCase(userInterface);
        this.realizarAluguelUseCase = new RealizarAluguelUseCase(userInterface);
        this.registrarDevolucaoUseCase = new RegistrarDevolucaoUseCase(userInterface);
        this.consultarAluguelAtivoUseCase = new ConsultarAluguelAtivoUseCase(userInterface);
        this.calcularMultaPorAtrasoUseCase = new CalcularMultaPorAtrasoUseCase(userInterface);
        this.listarHistoricoAlugueisUseCase = new ListarHistoricoAlugueisUseCase(userInterface);
    }

    public void gerenciarUsuarios() {
        Integer selected = this.userInterface.showMenu(MenuType.USER);
        if (selected == null) {
            return;
        }

        switch (selected) {
            case 1 -> cadastrarUsuarioUseCase.execute(usuarios);
            case 2 -> consultarUsuarioPorCpfUseCase.execute(usuarios);
            case 3 -> editarUsuarioUseCase.execute(usuarios);
            case 4 -> excluirUsuarioUseCase.execute(usuarios, transacoes);
            case 5 -> listarUsuariosUseCase.execute(usuarios);
        }

        if (MenuUsuario.findByValue(selected) != MenuUsuario.VOLTAR) {
            this.gerenciarUsuarios();
        }
    }

    public void gerenciarFerramentas() {
        Integer selected = this.userInterface.showMenu(MenuType.TOOL);
        if (selected == null) {
            return;
        }

        switch (selected) {
            case 1 -> cadastrarFerramentaUseCase.execute(usuarios, ferramentas);
            case 2 -> buscarFerramentaUseCase.execute(ferramentas);
            case 3 -> alterarPrecoFerramentaUseCase.execute(ferramentas);
            case 4 -> atualizarStatusFerramentaUseCase.execute(usuarios, ferramentas);
            case 5 -> listarFerramentasDisponiveisUseCase.execute(ferramentas);
        }

        if (MenuFerramenta.findByValue(selected) != MenuFerramenta.VOLTAR) {
            this.gerenciarFerramentas();
        }
    }

    public void gerenciarAlugueis() {
        Integer selected = this.userInterface.showMenu(MenuType.TRANSACTION);
        if (selected == null) {
            return;
        }

        switch (selected) {
            case 1 -> realizarAluguelUseCase.execute(usuarios, ferramentas, transacoes);
            case 2 -> registrarDevolucaoUseCase.execute(transacoes);
            case 3 -> consultarAluguelAtivoUseCase.execute(usuarios, transacoes);
            case 4 -> calcularMultaPorAtrasoUseCase.execute(transacoes);
            case 5 -> listarHistoricoAlugueisUseCase.execute(transacoes);
        }

        if (MenuAluguel.findByValue(selected) != MenuAluguel.VOLTAR) {
            this.gerenciarAlugueis();
        }
    }
}