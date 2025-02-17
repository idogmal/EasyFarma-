package view;

import dao.EstoqueDAO;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Estoque;

import java.util.Map;

public class VisualizarEstoqueView extends Application {

    // Limite para alerta de estoque baixo (exemplo: 10 unidades)
    private static final int LIMITE_ESTOQUE_BAIXO = 10;

    // TableView para exibir os dados
    private TableView<MedicamentoView> tableViewEstoque;
    // Lista observável para alimentar a TableView
    private ObservableList<MedicamentoView> medicamentoList;

    // Classe auxiliar para representar cada linha da tabela
    public static class MedicamentoView {
        private final String nome;
        private final int quantidade;
        private final String alerta;

        public MedicamentoView(String nome, int quantidade, String alerta) {
            this.nome = nome;
            this.quantidade = quantidade;
            this.alerta = alerta;
        }

        public String getNome() {
            return nome;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public String getAlerta() {
            return alerta;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Visualização de Estoque");

        // Inicializa a TableView e a lista de dados
        tableViewEstoque = new TableView<>();
        medicamentoList = FXCollections.observableArrayList();

        // Cria as colunas da TableView

        // Coluna: Nome do Medicamento
        TableColumn<MedicamentoView, String> colNome = new TableColumn<>("Nome do Medicamento");
        colNome.setMinWidth(200);
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        // Coluna: Quantidade
        TableColumn<MedicamentoView, Integer> colQuantidade = new TableColumn<>("Quantidade");
        colQuantidade.setMinWidth(150);
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        // Coluna: Alerta (exibe "Estoque Baixo" se a quantidade estiver abaixo do limite)
        TableColumn<MedicamentoView, String> colAlerta = new TableColumn<>("Alerta");
        colAlerta.setMinWidth(200);
        colAlerta.setCellValueFactory(new PropertyValueFactory<>("alerta"));

        tableViewEstoque.getColumns().addAll(colNome, colQuantidade, colAlerta);

        // Carrega os dados do estoque
        carregarDados();

        // Criação da área para adicionar novos medicamentos
        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do Medicamento");

        TextField txtQuantidade = new TextField();
        txtQuantidade.setPromptText("Quantidade");

        Button btnAdicionar = new Button("Adicionar Medicamento");
        btnAdicionar.setOnAction(e -> {
            String nome = txtNome.getText().trim();
            String quantidadeStr = txtQuantidade.getText().trim();

            if (nome.isEmpty() || quantidadeStr.isEmpty()) {
                System.out.println("Informe o nome e a quantidade.");
                return;
            }

            try {
                int quantidade = Integer.parseInt(quantidadeStr);
                if (quantidade <= 0) {
                    System.out.println("A quantidade deve ser maior que zero.");
                    return;
                }
                // Adiciona o medicamento utilizando o EstoqueDAO
                EstoqueDAO estoqueDAO = new EstoqueDAO();
                estoqueDAO.adicionarMedicamento(nome, quantidade);
                // Recarrega os dados da TableView
                carregarDados();
                // Limpa os campos de entrada
                txtNome.clear();
                txtQuantidade.clear();
            } catch (NumberFormatException ex) {
                System.out.println("Quantidade inválida. Informe um número.");
            }
        });

        // Layout para a área de entrada de dados
        HBox inputLayout = new HBox(10);
        inputLayout.getChildren().addAll(txtNome, txtQuantidade, btnAdicionar);
        inputLayout.setAlignment(Pos.CENTER);

        // Layout principal (vertical)
        VBox layout = new VBox(15);
        layout.getChildren().addAll(inputLayout, tableViewEstoque);
        layout.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(layout, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Método para carregar os dados do estoque utilizando o EstoqueDAO
    private void carregarDados() {
        EstoqueDAO estoqueDAO = new EstoqueDAO();
        Estoque estoque = estoqueDAO.getEstoque();
        Map<String, Integer> medicamentos = estoque.getMedicamentos();

        medicamentoList.clear();
        for (Map.Entry<String, Integer> entry : medicamentos.entrySet()) {
            String nome = entry.getKey();
            int quantidade = entry.getValue();
            String alerta = quantidade < LIMITE_ESTOQUE_BAIXO ? "Estoque Baixo" : "";
            medicamentoList.add(new MedicamentoView(nome, quantidade, alerta));
        }
        tableViewEstoque.setItems(medicamentoList);
    }

    // Método main para testar a tela de forma independente
    public static void main(String[] args) {
        launch(args);
    }
}
