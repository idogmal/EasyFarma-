package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class UsuarioDAO {
    private static final String ARQUIVO_USUARIOS = "usuarios.json";

    /**
     * Carrega os usuários salvos do arquivo JSON.
     * Se o arquivo não existir, retorna um mapa vazio.
     */
    public Map<String, String> carregarUsuarios() {
        File file = new File(ARQUIVO_USUARIOS);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, String>>() {}.getType();
            Map<String, String> usuarios = gson.fromJson(reader, type);
            return usuarios != null ? usuarios : new HashMap<>();
        } catch (IOException e) {
            System.err.println("Erro ao carregar usuários: " + e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * Salva o mapa de usuários no arquivo JSON.
     */
    public void salvarUsuarios(Map<String, String> usuarios) {
        try (FileWriter writer = new FileWriter(ARQUIVO_USUARIOS)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(usuarios, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar usuários: " + e.getMessage());
        }
    }
}
