package controller;

import dao.ReceitaDAO;
import dao.EstoqueDAO;
import model.Receita;

import java.util.HashMap;
import java.util.Map;

public class ReceitaController {
    private ReceitaDAO receitaDAO;
    private EstoqueDAO estoqueDAO;
    private String dataPrescricao;

    // Construtor
    public ReceitaController(ReceitaDAO receitaDAO, EstoqueDAO estoqueDAO) {
        this.receitaDAO = receitaDAO;
        this.estoqueDAO = estoqueDAO;
    }

    // Cadastrar uma nova receita
    public void cadastrarReceita(int id, String paciente, String cpf, String medicamentosEntrada) {
        // Processar os medicamentos inseridos pelo usuário
        Map<String, Integer> medicamentos = processarEntradaMedicamentos(medicamentosEntrada);

        if (medicamentos.isEmpty()) {
            System.out.println("Erro: Nenhum medicamento válido foi inserido.");
            return;
        }

        // Criar a receita e salvá-la
        Receita receita = new Receita(id, paciente, cpf, medicamentos, dataPrescricao);
        receitaDAO.adicionarReceita(receita);
        System.out.println("Receita cadastrada com sucesso!");

        // Atualizar o estoque com os medicamentos da receita
        medicamentos.forEach((medicamento, quantidade) -> {
            estoqueDAO.adicionarMedicamento(medicamento, quantidade);
            System.out.println("Estoque atualizado: " + medicamento + " (" + quantidade + " unidades adicionadas)");
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
                    System.out.println("Erro ao processar medicamento: " + item);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao interpretar a entrada de medicamentos.");
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

    // Validar uma receita
    public void validarReceita(int id) {
        Receita receita = receitaDAO.buscarReceitaPorId(id);
        if (receita != null) {
            receita.validar();
            receitaDAO.salvarAlteracoes(); // Salvar mudanças no arquivo
            System.out.println("Receita validada com sucesso para o paciente: " + receita.getPaciente());

            // Atualizar o estoque, reduzindo a quantidade dos medicamentos
            receita.getMedicamentos().forEach((medicamento, quantidade) -> {
                boolean atualizado = estoqueDAO.diminuirEstoque(medicamento, quantidade);
                if (atualizado) {
                    System.out.println("Estoque reduzido para " + medicamento + ": " + quantidade + " unidades.");
                } else {
                    System.out.println("Erro: Estoque insuficiente para o medicamento: " + medicamento);
                }
            });
        } else {
            System.out.println("Receita com ID " + id + " não encontrada.");
        }
    }

    // Consultar o estoque de um medicamento
    public void consultarEstoque(String medicamento) {
        int quantidade = estoqueDAO.consultarEstoque(medicamento.toLowerCase());
        if (quantidade > 0) {
            System.out.println("Estoque de " + medicamento + ": " + quantidade + " unidades.");
        } else {
            System.out.println("Medicamento " + medicamento + " não encontrado no estoque.");
        }
    }
}
