package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Receita;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ReceitaDAO {
    private List<Receita> receitas;
    private static final String ARQUIVO_RECEITAS = "receitas.json";

    public ReceitaDAO() {
        this.receitas = carregarReceitas();
    }

    public List<Receita> listarReceitas() {
        return receitas;
    }

    public void adicionarReceita(Receita receita) {
        receitas.add(receita);
        salvarReceitas();
    }

    public Receita buscarReceitaPorId(int id) {
        for (Receita receita : receitas) {
            if (receita.getId() == id) {
                return receita;
            }
        }
        return null; // Retorna null caso a receita não seja encontrada
    }

    public void salvarAlteracoes() {
        salvarReceitas();
    }

    private void salvarReceitas() {
        try (FileWriter writer = new FileWriter(ARQUIVO_RECEITAS)) {
            Gson gson = new Gson();
            gson.toJson(receitas, writer);
            System.out.println("Receitas salvas com sucesso no arquivo: " + ARQUIVO_RECEITAS);
        } catch (IOException e) {
            System.err.println("Erro ao salvar receitas: " + e.getMessage());
        }
    }

    private List<Receita> carregarReceitas() {
        File file = new File(ARQUIVO_RECEITAS);
        if (!file.exists()) {
            System.out.println("Arquivo " + ARQUIVO_RECEITAS + " não encontrado. Iniciando com lista vazia.");
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Receita>>() {}.getType();
            List<Receita> receitasCarregadas = gson.fromJson(reader, listType);
            System.out.println("Receitas carregadas com sucesso do arquivo: " + ARQUIVO_RECEITAS);
            return receitasCarregadas != null ? receitasCarregadas : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao carregar receitas: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
