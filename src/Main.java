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
            System.out.println("\n=== Sistema de Gerenciamento de Receitas ===");
            System.out.println("1. Cadastrar Receita");
            System.out.println("2. Listar Receitas");
            System.out.println("3. Validar Receita");
            System.out.println("4. Atualizar Estoque");
            System.out.println("5. Consultar Estoque");
            System.out.println("6. Finalizar Compra e Reduzir Estoque");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            while (!scanner.hasNextInt()) { // Validação para evitar erro de entrada
                System.out.println("Por favor, digite um número válido.");
                scanner.next(); // Consumir entrada inválida
            }
            opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha

            switch (opcao) {
                case 1:
                    cadastrarReceita(scanner, receitaController);
                    break;
                case 2:
                    receitaController.listarReceitas();
                    break;
                case 3:
                    validarReceita(scanner, receitaController);
                    break;
                case 4:
                    atualizarEstoque(scanner, estoqueDAO);
                    break;
                case 5:
                    consultarEstoque(scanner, estoqueDAO);
                    break;
                case 6:
                    finalizarCompra(scanner, estoqueDAO);
                    break;
                case 0:
                    System.out.println("Encerrando o sistema...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);

        scanner.close();
    }

    private static void cadastrarReceita(Scanner scanner, ReceitaController receitaController) {
        try {
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
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar receita: " + e.getMessage());
            scanner.nextLine(); // Consumir entrada inválida
        }
    }

    private static void validarReceita(Scanner scanner, ReceitaController receitaController) {
        try {
            System.out.print("ID da Receita para Validar: ");
            int idValidar = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha

            receitaController.validarReceita(idValidar);
        } catch (Exception e) {
            System.out.println("Erro ao validar receita: " + e.getMessage());
            scanner.nextLine(); // Consumir entrada inválida
        }
    }

    private static void atualizarEstoque(Scanner scanner, EstoqueDAO estoqueDAO) {
        try {
            System.out.print("Medicamento para Atualizar: ");
            String medAtualizar = scanner.nextLine().trim();

            System.out.print("Quantidade para Adicionar: ");
            int quantidade = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha

            estoqueDAO.adicionarMedicamento(medAtualizar, quantidade);
            System.out.println("Estoque atualizado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao atualizar estoque: " + e.getMessage());
            scanner.nextLine(); // Consumir entrada inválida
        }
    }

    private static void consultarEstoque(Scanner scanner, EstoqueDAO estoqueDAO) {
        try {
            System.out.print("Medicamento para Consultar: ");
            String medConsultar = scanner.nextLine().trim();

            int quantidadeEstoque = estoqueDAO.consultarEstoque(medConsultar);
            System.out.println("Quantidade disponível: " + quantidadeEstoque);
        } catch (Exception e) {
            System.out.println("Erro ao consultar estoque: " + e.getMessage());
            scanner.nextLine(); // Consumir entrada inválida
        }
    }

    private static void finalizarCompra(Scanner scanner, EstoqueDAO estoqueDAO) {
        try {
            System.out.print("Medicamento comprado: ");
            String medicamento = scanner.nextLine().trim();

            System.out.print("Quantidade comprada: ");
            int quantidade = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha

            if (estoqueDAO.consultarEstoque(medicamento) == 0) {
                System.out.println("Erro: O medicamento não existe no estoque.");
                return;
            }

            boolean sucesso = estoqueDAO.diminuirEstoque(medicamento, quantidade);
            if (sucesso) {
                System.out.println("Compra finalizada! Estoque atualizado.");
            } else {
                System.out.println("Erro: Estoque insuficiente para concluir a compra.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao finalizar compra: " + e.getMessage());
            scanner.nextLine(); // Consumir entrada inválida
        }
    }
}
