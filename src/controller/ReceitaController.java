package controller;

import dao.ReceitaDAO;
import dao.EstoqueDAO;
import model.Receita;

import java.util.HashMap;
import java.util.Map;

public class ReceitaController {
    private final ReceitaDAO receitaDAO;
    private final EstoqueDAO estoqueDAO;

    // Construtor
    public ReceitaController(ReceitaDAO receitaDAO, EstoqueDAO estoqueDAO) {
        this.receitaDAO = receitaDAO;
        this.estoqueDAO = estoqueDAO;
    }

    // Cadastrar uma nova receita (ID gerado automaticamente)
    public void cadastrarReceita(String paciente, String cpf, String crm, String medicamentosEntrada, String dataPrescricao) {
        if (paciente.isEmpty() || cpf.isEmpty() || crm.isEmpty() || medicamentosEntrada.isEmpty() || dataPrescricao.isEmpty()) {
            System.out.println("Erro: Todos os campos são obrigatórios.");
            return;
        }

        // Processar os medicamentos inseridos pelo usuário
        Map<String, Integer> medicamentos = processarEntradaMedicamentos(medicamentosEntrada);

        if (medicamentos.isEmpty()) {
            System.out.println("Erro: Nenhum medicamento válido foi inserido.");
            return;
        }

        // Gerar um ID único para a receita
        int id = receitaDAO.listarReceitas().size() + 1;

        // Criar a receita e salvá-la
        Receita receita = new Receita(id, paciente, cpf, medicamentos, dataPrescricao);
        receitaDAO.adicionarReceita(receita);
        System.out.println("Receita cadastrada com sucesso! ID: " + id);

        // Atualizar o estoque com os medicamentos da receita, diminuindo a quantidade
        medicamentos.forEach((medicamento, quantidade) -> {
            boolean sucesso = estoqueDAO.diminuirEstoque(medicamento, quantidade);
            if (sucesso) {
                System.out.println("Estoque atualizado: " + medicamento + " (" + quantidade + " unidades subtraídas)");
            } else {
                System.out.println("Estoque insuficiente para: " + medicamento);
            }
        });
    }


    // Processar a entrada de medicamentos e convertê-los para um Map<String, Integer>
    private Map<String, Integer> processarEntradaMedicamentos(String entrada) {
        Map<String, Integer> medicamentos = new HashMap<>();

        try {
            String[] itens = entrada.split(","); // Medicamentos separados por vírgula
            for (String item : itens) {
                String[] partes = item.trim().split(" "); // Exemplo: "Ibuprofeno 2"
                if (partes.length == 2) {
                    String nomeMedicamento = partes[0].trim().toLowerCase();
                    int quantidade = Integer.parseInt(partes[1].trim());
                    if (quantidade > 0) {
                        medicamentos.put(nomeMedicamento, quantidade);
                    } else {
                        System.out.println("Quantidade inválida para o medicamento: " + nomeMedicamento);
                    }
                } else {
                    System.out.println("Erro ao processar medicamento: " + item + " (Formato esperado: Nome Quantidade)");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao interpretar a entrada de medicamentos: " + e.getMessage());
        }

        return medicamentos;
    }

    // Listar todas as receitas
    public void listarReceitas() {
        if (receitaDAO.listarReceitas().isEmpty()) {
            System.out.println("Nenhuma receita cadastrada.");
        } else {
            System.out.println("\n=== Lista de Receitas ===");
            for (Receita receita : receitaDAO.listarReceitas()) {
                System.out.println(receita);
                System.out.println("-------------------------");
            }
        }
    }
}
