package model;

import java.util.HashMap;

public class Estoque {
    private HashMap<String, Integer> medicamentos;

    // Construtor
    public Estoque() {
        medicamentos = new HashMap<>();
    }

    // Adicionar medicamento ao estoque (ou aumentar quantidade existente)
    public void adicionarMedicamento(String nome, int quantidade) {
        if (nome == null || nome.isEmpty() || quantidade <= 0) {
            throw new IllegalArgumentException("Nome do medicamento e quantidade devem ser válidos.");
        }
        String nomePadronizado = nome.toLowerCase(); // Padroniza o nome
        medicamentos.put(nomePadronizado, medicamentos.getOrDefault(nomePadronizado, 0) + quantidade);
    }

    // Atualizar (reduzir) o estoque de um medicamento
    public boolean atualizarEstoque(String nome, int quantidade) {
        if (nome == null || nome.isEmpty() || quantidade <= 0) {
            throw new IllegalArgumentException("Nome do medicamento e quantidade devem ser válidos.");
        }

        String nomePadronizado = nome.toLowerCase(); // Padroniza o nome
        int atual = medicamentos.getOrDefault(nomePadronizado, 0);
        if (atual >= quantidade) {
            medicamentos.put(nomePadronizado, atual - quantidade);
            return true; // Estoque atualizado com sucesso
        }
        return false; // Estoque insuficiente
    }

    // Consultar quantidade disponível de um medicamento
    public int consultarEstoque(String nome) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Nome do medicamento deve ser válido.");
        }

        String nomePadronizado = nome.toLowerCase(); // Padroniza o nome
        return medicamentos.getOrDefault(nomePadronizado, 0);
    }

    // Exibir o estoque completo (para depuração ou listagem)
    public void exibirEstoque() {
        System.out.println("Estoque Atual:");
        medicamentos.forEach((nome, quantidade) ->
                System.out.println("Medicamento: " + nome + ", Quantidade: " + quantidade)
        );
    }
}
