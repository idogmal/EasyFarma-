package view;

import dao.EstoqueDAO;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Estoque;

import java.util.Map;

public class VisualizarEstoqueView extends Application {

    private TableView<MedicamentoView> tableViewEstoque;
    private ObservableList<MedicamentoView> medicamentoList;
    private EstoqueDAO estoqueDAO;

    // Classe auxiliar para representar cada medicamento na tabela
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
        primaryStage.setTitle("EasyFarma - Visualizar Estoque");

        // Inicializa o DAO e a lista observável
        estoqueDAO = new EstoqueDAO();
        medicamentoList = FXCollections.observableArrayList();
        tableViewEstoque = new TableView<>();

        // ===================== MENU LATERAL (verde) =====================
        VBox menuLateral = new VBox(15);
        menuLateral.setPadding(new Insets(20));
        menuLateral.setStyle("-fx-background-color: #2E7D32;");
        menuLateral.setPrefWidth(180);

        Button btnCadastrarReceita = criarBotaoMenu("Cadastrar Receita", () -> {
            // Para consistência, crie uma instância compartilhada do controller se necessário
            new CadastrarReceita(new controller.ReceitaController(new dao.ReceitaDAO(), new dao.EstoqueDAO())).start(new Stage());
            primaryStage.close();
        });
        Button btnPesquisarReceita = criarBotaoMenu("Pesquisar Receita", () -> {
            new PesquisarReceita().start(new Stage());
            primaryStage.close();
        });
        // O botão "Estoque" não é necessário nesta tela – podemos desabilitá-lo
        Button btnEstoque = criarBotaoMenu("Estoque", () -> {});
        btnEstoque.setDisable(true);
        Button btnSair = criarBotaoMenu("Sair", () -> primaryStage.close());
        menuLateral.getChildren().addAll(btnCadastrarReceita, btnPesquisarReceita, btnEstoque, btnSair);

        // ===================== CONTEÚDO CENTRAL =====================
        VBox conteudoCentral = new VBox(20);
        conteudoCentral.setPadding(new Insets(30));
        conteudoCentral.setAlignment(Pos.TOP_CENTER);

        Label lblTitulo = new Label("Visualizar Estoque");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Campo de pesquisa para medicamentos
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);
        TextField txtPesquisar = new TextField();
        txtPesquisar.setPromptText("Pesquisar Medicamento");
        Button btnPesquisarMed = new Button("Buscar");
        btnPesquisarMed.setOnAction(e -> pesquisarMedicamento(txtPesquisar.getText().trim()));
        searchBox.getChildren().addAll(new Label("Pesquisar:"), txtPesquisar, btnPesquisarMed);

        // Configuração da TableView
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

        carregarDados();

        // Formulário para adicionar um novo medicamento
        HBox addBox = new HBox(10);
        addBox.setAlignment(Pos.CENTER);
        TextField txtNomeMed = new TextField();
        txtNomeMed.setPromptText("Nome do Medicamento");
        TextField txtQuantidade = new TextField();
        txtQuantidade.setPromptText("Quantidade");
        Button btnAdicionar = new Button("Adicionar Medicamento");
        btnAdicionar.setOnAction(e -> {
            String nome = txtNomeMed.getText().trim();
            String qtdStr = txtQuantidade.getText().trim();
            if (nome.isEmpty() || qtdStr.isEmpty()) {
                showAlert("Informe o nome e a quantidade.");
                return;
            }
            try {
                int quantidade = Integer.parseInt(qtdStr);
                if (quantidade <= 0) {
                    showAlert("A quantidade deve ser maior que zero.");
                    return;
                }
                // Adiciona medicamento ao estoque e recarrega os dados
                estoqueDAO.adicionarMedicamento(nome, quantidade);
                carregarDados();
                txtNomeMed.clear();
                txtQuantidade.clear();
            } catch (NumberFormatException ex) {
                showAlert("Quantidade inválida. Informe um número.");
            }
        });
        addBox.getChildren().addAll(txtNomeMed, txtQuantidade, btnAdicionar);

        VBox centerContainer = new VBox(20, lblTitulo, searchBox, tableViewEstoque, addBox);
        centerContainer.setAlignment(Pos.TOP_CENTER);
        conteudoCentral.getChildren().add(centerContainer);

        // ===================== LOGO NO RODAPÉ (inferior direito) =====================
        ImageView logoView = null;
        try {
            Image logo = new Image(getClass().getResourceAsStream("/logo.png"));
            logoView = new ImageView(logo);
            logoView.setFitHeight(50);
            logoView.setPreserveRatio(true);
        } catch(Exception ex) {
            System.err.println("Logo não encontrada!");
        }
        HBox bottomBar = new HBox();
        bottomBar.setPadding(new Insets(10));
        bottomBar.setAlignment(Pos.BOTTOM_RIGHT);
        if (logoView != null) {
            bottomBar.getChildren().add(logoView);
        }

        // ===================== LAYOUT PRINCIPAL (BorderPane) =====================
        BorderPane root = new BorderPane();
        root.setLeft(menuLateral);
        root.setCenter(conteudoCentral);
        root.setBottom(bottomBar);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Carrega os dados do estoque na tabela
    private void carregarDados() {
        medicamentoList.clear();
        Map<String, Integer> medicamentos = estoqueDAO.getEstoque().getMedicamentos();
        for (Map.Entry<String, Integer> entry : medicamentos.entrySet()) {
            String nome = entry.getKey();
            int quantidade = entry.getValue();
            String alerta = quantidade < 10 ? "Estoque Baixo" : "";
            medicamentoList.add(new MedicamentoView(nome, quantidade, alerta));
        }
        tableViewEstoque.setItems(medicamentoList);
    }

    // Pesquisa medicamentos pelo nome (caso o campo esteja vazio, exibe todos)
    private void pesquisarMedicamento(String filtro) {
        if (filtro.isEmpty()) {
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

    // Cria um botão para o menu lateral com ação
    private Button criarBotaoMenu(String texto, Runnable acao) {
        Button btn = new Button(texto);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16px;");
        btn.setOnAction(e -> acao.run());
        return btn;
    }

    // Exibe um alerta informativo
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
