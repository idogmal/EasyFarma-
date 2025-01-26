import controller.ReceitaController;
import dao.ReceitaDAO;
import dao.EstoqueDAO;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ReceitaDAO receitaDAO = new ReceitaDAO();
        EstoqueDAO estoqueDAO = new EstoqueDAO();
        ReceitaController receitaController = new ReceitaController(receitaDAO, estoqueDAO);

        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n=== Sistema de Gerenciamento de Receitas ===");2
            System.out.println("1. Cadastrar Receita");
            System.out.println("2. Listar Receitas");
            System.out.println("3. Validar Receita");
            System.out.println("4. Atualizar Estoque");
            System.out.println("5. Consultar Estoque");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha

            switch (opcao) {
                case 1:
                    System.out.print("ID da Receita: ");
                    int id = scanner.nextInt();
                    scanner.nextLine(); // Consumir a quebra de linha
                    System.out.print("Nome do Paciente: ");
                    String paciente = scanner.nextLine().trim();
                    System.out.print("Medicamento: ");
                    String medicamento = scanner.nextLine().trim();
                    System.out.print("Data da Prescrição: ");
                    String data = scanner.nextLine().trim();

                    receitaController.cadastrarReceita(id, paciente, medicamento, data);
                    break;

                case 2:
                    receitaController.listarReceitas();
                    break;

                case 3:
                    System.out.print("ID da Receita para Validar: ");
                    int idValidar = scanner.nextInt();
                    scanner.nextLine(); // Consumir a quebra de linha
                    receitaController.validarReceita(idValidar);
                    break;

                case 4:
                    System.out.print("Medicamento para Atualizar: ");
                    String medAtualizar = scanner.nextLine().trim();
                    System.out.print("Quantidade para Adicionar: ");
                    int quantidade = scanner.nextInt();
                    scanner.nextLine(); // Consumir a quebra de linha
                    estoqueDAO.adicionarMedicamento(medAtualizar, quantidade);
                    System.out.println("Estoque atualizado com sucesso!");
                    break;

                case 5:
                    System.out.print("Medicamento para Consultar: ");
                    String medConsultar = scanner.nextLine().trim();
                    int quantidadeEstoque = estoqueDAO.consultarEstoque(medConsultar);
                    System.out.println("Quantidade disponível: " + quantidadeEstoque);
                    break;

                case 0:
                    System.out.println("Encerrando o sistema...");
                    break;

                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } while (opcao != 0);

        scanner.close();
    }
}
