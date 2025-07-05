package services;

import models.Ferramenta;
import models.Transacao;
import models.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArmazenamentoService {
    private static final String USUARIOS_FILE = "usuarios.dat";
    private static final String FERRAMENTAS_FILE = "ferramentas.dat";
    private static final String TRANSACOES_FILE = "transacoes.dat";

    public void salvarDados(List<Usuario> usuarios, List<Ferramenta> ferramentas, List<Transacao> transacoes) {
        salvarObjeto(usuarios, USUARIOS_FILE);
        salvarObjeto(ferramentas, FERRAMENTAS_FILE);
        salvarObjeto(transacoes, TRANSACOES_FILE);
    }

    public Map<String, List<?>> carregarDados() {
        Map<String, List<?>> dados = new HashMap<>();
        dados.put("usuarios", carregarObjeto(USUARIOS_FILE));
        dados.put("ferramentas", carregarObjeto(FERRAMENTAS_FILE));
        dados.put("transacoes", carregarObjeto(TRANSACOES_FILE));
        return dados;
    }

    private void salvarObjeto(Object obj, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(obj);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados em " + filename + ": " + e.getMessage());
        }
    }

    private List<?> carregarObjeto(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<?>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados de " + filename + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}