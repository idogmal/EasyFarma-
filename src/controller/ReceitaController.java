package controller;

import dao.ReceitaDAO;
import dao.EstoqueDAO;
import model.Receita;

public class ReceitaController {
    private ReceitaDAO receitaDAO;
    private EstoqueDAO estoqueDAO;

    // Construtor
    public ReceitaController(ReceitaDAO receitaDAO, EstoqueDAO estoqueDAO) {
        this.receitaDAO = receitaDAO;
        this.estoqueDAO = estoqueDAO;
    }

    // Cadastrar uma nova receita
    public void cadastrarReceita(int id, String paciente, String medicamento, String dataPrescricao) {
        Receita receita = new Receita(id, paciente, medicamento, dataPrescricao);
        receitaDAO.adicionarReceita(receita);
        System.out.println("Receita cadastrada com sucesso!");
    }

    // Listar todas as receitas
    public void listarReceitas() {
        if (receitaDAO.listarReceitas().isEmpty()) {
            System.out.println("Nenhuma receita cadastrada.");
        } else {
            for (Receita receita : receitaDAO.listarReceitas()) {
                System.out.println("ID: " + receita.getId() +
                        ", Paciente: " + receita.getPaciente() +
                        ", Medicamento: " + receita.getMedicamento() +
                        ", Data: " + receita.getDataPrescricao() +
                        ", Validada: " + (receita.isValidada() ? "Sim" : "Não"));
            }
        }
    }

    // Validar uma receita
    public void validarReceita(int id) {
        Receita receita = receitaDAO.buscarReceitaPorId(id);
        if (receita != null) {
            receita.validar();
            receitaDAO.salvarAlteracoes(); // Salvar mudanças no arquivo
            System.out.println("Receita validada com sucesso: " + receita.getPaciente());
        } else {
            System.out.println("Receita com ID " + id + " não encontrada.");
        }
    }

    // Consultar o estoque do medicamento relacionado à receita
    public void consultarEstoque(String medicamento) {
        int quantidade = estoqueDAO.consultarEstoque(medicamento);
        System.out.println("Estoque de " + medicamento + ": " + quantidade + " unidades.");
    }
}
