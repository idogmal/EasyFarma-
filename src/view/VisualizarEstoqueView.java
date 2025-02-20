package view;

import dao.EstoqueDAO;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    // Tabela para exibir os dados do estoque
    private TableView<MedicamentoView> tableViewEstoque;
    // Lista observável que armazena os medicamentos carregados
    private ObservableList<MedicamentoView> medicamentoList;
    // DAO para manipulação do estoque
    private EstoqueDAO estoqueDAO;

    /**
     * Classe auxiliar que representa cada medicamento na tabela.
     */
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

        estoqueDAO = new EstoqueDAO();
        medicamentoList = FXCollections.observableArrayList();
        tableViewEstoque = new TableView<>();

        // Criação das colunas da TableView
        TableColumn<MedicamentoView, String> colNome = new TableColumn<>("Nome do Medicamento");
        colNome.setMinWidth(200);
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<MedicamentoView, Integer> colQuantidade = new TableColumn<>("Quantidade");
        colQuantidade.setMinWidth(150);
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        TableColumn<MedicamentoView, String> colAlerta = new TableColumn<>("Alerta");
        colAlerta.setMinWidth(200);
        colAlerta.setCellValueFactory(new PropertyValueFactory<>("alerta"));

        tableViewEstoque.getColumns().addAll(colNome, colQuantidade, colAlerta);

        // Carrega os dados do estoque
        carregarDados();

        // Criação do campo de pesquisa de medicamento
        TextField txtPesquisar = new TextField();
        txtPesquisar.setPromptText("Pesquisar Medicamento");
        Button btnPesquisar = new Button("Pesquisar");
        btnPesquisar.setOnAction(e -> pesquisarMedicamento(txtPesquisar.getText().trim()));

        HBox pesquisaBox = new HBox(10, new Label("Pesquisar:"), txtPesquisar, btnPesquisar);
        pesquisaBox.setAlignment(Pos.CENTER);
        pesquisaBox.setPadding(new Insets(10));

        // Área para adicionar novos medicamentos
        TextField txtNomeMedicamento = new TextField();
        txtNomeMedicamento.setPromptText("Nome do Medicamento");

        TextField txtQuantidade = new TextField();
        txtQuantidade.setPromptText("Quantidade");

        Button btnAdicionar = new Button("Adicionar Medicamento");
        btnAdicionar.setOnAction(e -> {
            String nome = txtNomeMedicamento.getText().trim();
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
                // Adiciona o medicamento ao estoque
                estoqueDAO.adicionarMedicamento(nome, quantidade);
                // Recarrega os dados para atualizar a tabela
                carregarDados();
                txtNomeMedicamento.clear();
                txtQuantidade.clear();
            } catch (NumberFormatException ex) {
                System.out.println("Quantidade inválida. Informe um número.");
            }
        });

        HBox addBox = new HBox(10, txtNomeMedicamento, txtQuantidade, btnAdicionar);
        addBox.setAlignment(Pos.CENTER);
        addBox.setPadding(new Insets(10));

        // Layout principal: campo de pesquisa, tabela e área de adição
        VBox layout = new VBox(10, pesquisaBox, tableViewEstoque, addBox);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Carrega os dados do estoque utilizando o EstoqueDAO e popula a lista observável.
     */
    private void carregarDados() {
        medicamentoList.clear();
        Estoque estoque = estoqueDAO.getEstoque();
        Map<String, Integer> medicamentos = estoque.getMedicamentos();
        for (Map.Entry<String, Integer> entry : medicamentos.entrySet()) {
            String nome = entry.getKey();
            int quantidade = entry.getValue();
            String alerta = quantidade < LIMITE_ESTOQUE_BAIXO ? "Estoque Baixo" : "";
            medicamentoList.add(new MedicamentoView(nome, quantidade, alerta));
        }
        tableViewEstoque.setItems(medicamentoList);
    }

    /**
     * Filtra os medicamentos da tabela com base no nome informado.
     *
     * @param filtro Texto a ser pesquisado no nome do medicamento.
     */
    private void pesquisarMedicamento(String filtro) {
        if (filtro.isEmpty()) {
            // Se o campo de pesquisa estiver vazio, exibe todos os medicamentos
            tableViewEstoque.setItems(medicamentoList);
        } else {
            ObservableList<MedicamentoView> filtrado = FXCollections.observableArrayList();
            for (MedicamentoView m : medicamentoList) {
                if (m.getNome().toLowerCase().contains(filtro.toLowerCase())) {
                    filtrado.add(m);
                }
            }
            tableViewEstoque.setItems(filtrado);
        }
    }

    // Método main para testar a tela de forma independente
    public static void main(String[] args) {
        launch(args);
    }
}
