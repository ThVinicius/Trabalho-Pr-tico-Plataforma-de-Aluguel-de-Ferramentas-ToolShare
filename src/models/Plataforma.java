package models;

import enums.MenuAluguel;
import enums.MenuFerramenta;
import enums.MenuType;
import enums.MenuUsuario;
import exceptions.FerramentaIndisponivelException;
import exceptions.FormatoDadosException;
import exceptions.SaldoInsuficienteException;
import exceptions.ValidacaoException;
import interfaces.IUserInterface;
import services.ArmazenamentoService;
import usecases.aluguel.*;
import usecases.ferramenta.*;
import usecases.relatorio.GerarRelatorioFaturamentoUseCase;
import usecases.usuario.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Plataforma {
    private final IUserInterface userInterface;
    private final ArmazenamentoService armazenamentoService;

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

    private final GerarRelatorioFaturamentoUseCase gerarRelatorioFaturamentoUseCase;

    private List<Ferramenta> ferramentas;
    private List<Transacao> transacoes;
    private List<Usuario> usuarios;

    public Plataforma(IUserInterface userInterface) {
        this.userInterface = userInterface;
        this.armazenamentoService = new ArmazenamentoService();
        this.carregarDados();

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

        this.gerarRelatorioFaturamentoUseCase = new GerarRelatorioFaturamentoUseCase(userInterface);
    }

    public void gerenciarUsuarios() {
        Integer selected = this.userInterface.showMenu(MenuType.USER);
        if (selected == null) {
            return;
        }

        try {
            switch (selected) {
                case 1 -> cadastrarUsuarioUseCase.execute(usuarios);
                case 2 -> consultarUsuarioPorCpfUseCase.execute(usuarios);
                case 3 -> editarUsuarioUseCase.execute(usuarios);
                case 4 -> excluirUsuarioUseCase.execute(usuarios, transacoes);
                case 5 -> listarUsuariosUseCase.execute(usuarios);
            }
        } catch (FormatoDadosException | ValidacaoException e) {
            userInterface.showError(e.getMessage());
        } catch (Exception e) {
            userInterface.showError("Ocorreu um erro inesperado no sistema.");
            e.printStackTrace();
        }

        if (MenuUsuario.findByValue(selected) != MenuUsuario.VOLTAR) {
            this.gerenciarUsuarios();
        }
    }

    public void gerenciarFerramentas() {
        Integer selected = this.userInterface.showMenu(MenuType.TOOL);
        if (selected == null || MenuFerramenta.findByValue(selected) == MenuFerramenta.VOLTAR) {
            return;
        }

        try {
            switch (selected) {
                case 1 -> cadastrarFerramentaUseCase.execute(usuarios, ferramentas);
                case 2 -> buscarFerramentaUseCase.execute(ferramentas);
                case 3 -> alterarPrecoFerramentaUseCase.execute(ferramentas);
                case 4 -> atualizarStatusFerramentaUseCase.execute(usuarios, ferramentas);
                case 5 -> listarFerramentasDisponiveisUseCase.execute(ferramentas);
            }
        } catch (FormatoDadosException | ValidacaoException | FerramentaIndisponivelException e) {
            userInterface.showError(e.getMessage());
        } catch (Exception e) {
            userInterface.showError("Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
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

        try {
            switch (selected) {
                case 1 -> realizarAluguelUseCase.execute(usuarios, ferramentas, transacoes);
                case 2 -> registrarDevolucaoUseCase.execute(transacoes);
                case 3 -> consultarAluguelAtivoUseCase.execute(usuarios, transacoes);
                case 4 -> calcularMultaPorAtrasoUseCase.execute(transacoes);
                case 5 -> listarHistoricoAlugueisUseCase.execute(transacoes);
            }
        } catch (FormatoDadosException | ValidacaoException | FerramentaIndisponivelException |
                 SaldoInsuficienteException e) {
            userInterface.showError(e.getMessage());
        } catch (Exception e) {
            userInterface.showError("Ocorreu um erro inesperado no sistema.");
            e.printStackTrace();
        }

        if (MenuAluguel.findByValue(selected) != MenuAluguel.VOLTAR) {
            this.gerenciarAlugueis();
        }
    }

    public void gerarRelatorio() {
        try {
            gerarRelatorioFaturamentoUseCase.execute(transacoes);
        } catch (FormatoDadosException e) {
            userInterface.showError(e.getMessage());
        } catch (Exception e) {
            userInterface.showError("Ocorreu um erro ao gerar o relatório: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarDados() {
        Map<String, List<?>> dados = armazenamentoService.carregarDados();
        this.usuarios = new ArrayList<>((List<Usuario>) dados.get("usuarios"));
        this.ferramentas = new ArrayList<>((List<Ferramenta>) dados.get("ferramentas"));
        this.transacoes = new ArrayList<>((List<Transacao>) dados.get("transacoes"));

        Transacao.atualizarProximoId(this.transacoes);
        Ferramenta.atualizarProximoCodigo(this.ferramentas);

        userInterface.showMessage("Info", "Dados carregados com sucesso!");
    }

    public void salvarDados() {
        armazenamentoService.salvarDados(usuarios, ferramentas, transacoes);
        userInterface.showMessage("Info", "Dados salvos com sucesso!");
    }
}