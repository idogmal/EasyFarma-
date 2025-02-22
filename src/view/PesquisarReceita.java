package view;

import dao.ReceitaDAO;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Receita;
import javafx.beans.property.SimpleStringProperty;
import util.ExportadorReceitas;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class PesquisarReceita extends Application {
    private TableView<Receita> table;
    private ObservableList<Receita> data;
    private ReceitaDAO receitaDAO = new ReceitaDAO();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("EasyFarma - Pesquisar Receita");

        // ===================== MENU LATERAL (verde) =====================
        VBox menuLateral = new VBox(15);
        menuLateral.setPadding(new Insets(20));
        menuLateral.setStyle("-fx-background-color: #2E7D32;");
        menuLateral.setPrefWidth(180);

        // Cria os botões do menu lateral para navegação
        Button btnCadastrarReceita = criarBotaoMenu("Cadastrar Receita", () -> {
            // Em um cenário real, compartilhe a instância do controller
            new CadastrarReceita(new controller.ReceitaController(new dao.ReceitaDAO(), new dao.EstoqueDAO())).start(new Stage());
            primaryStage.close();
        });
        Button btnPesquisarReceita = criarBotaoMenu("Pesquisar Receita", () -> {
            // Esta é a tela atual; desabilitamos este botão.
        });
        btnPesquisarReceita.setDisable(true);
        Button btnEstoque = criarBotaoMenu("Estoque", () -> {
            new VisualizarEstoqueView().start(new Stage());
            primaryStage.close();
        });
        Button btnSair = criarBotaoMenu("Sair", () -> primaryStage.close());
        menuLateral.getChildren().addAll(btnCadastrarReceita, btnPesquisarReceita, btnEstoque, btnSair);

        // ===================== CONTEÚDO CENTRAL =====================
        VBox conteudoCentral = new VBox(20);
        conteudoCentral.setPadding(new Insets(30));
        conteudoCentral.setAlignment(Pos.TOP_CENTER);

        Label lblTitulo = new Label("Pesquisar Receita");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Filtros de pesquisa
        GridPane gridFiltros = new GridPane();
        gridFiltros.setHgap(10);
        gridFiltros.setVgap(10);
        gridFiltros.setAlignment(Pos.CENTER);

        Label lblNome = new Label("Nome do Paciente:");
        lblNome.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        TextField txtNome = new TextField();
        txtNome.setPromptText("Digite o nome");

        Label lblCpf = new Label("CPF do Paciente:");
        lblCpf.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        TextField txtCpf = new TextField();
        txtCpf.setPromptText("Digite o CPF");

        gridFiltros.add(lblNome, 0, 0);
        gridFiltros.add(txtNome, 1, 0);
        gridFiltros.add(lblCpf, 0, 1);
        gridFiltros.add(txtCpf, 1, 1);

        // Botões de ação para pesquisa, exportação e validação
        HBox boxBotoes = new HBox(15);
        boxBotoes.setAlignment(Pos.CENTER);
        Button btnPesquisar = new Button("Pesquisar");
        Button btnExportar = new Button("Exportar Receitas");
        Button btnValidar = new Button("Validar Receita");
        boxBotoes.getChildren().addAll(btnPesquisar, btnExportar, btnValidar);

        // Tabela de receitas
        table = new TableView<>();
        data = FXCollections.observableArrayList();
        TableColumn<Receita, String> colPaciente = new TableColumn<>("Paciente");
        colPaciente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaciente()));
        TableColumn<Receita, String> colCpf = new TableColumn<>("CPF");
        colCpf.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCpf()));
        TableColumn<Receita, String> colMedicamentos = new TableColumn<>("Medicamentos");
        colMedicamentos.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMedicamentosAsString()));
        TableColumn<Receita, String> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataPrescricao()));
        TableColumn<Receita, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        table.getColumns().addAll(colPaciente, colCpf, colMedicamentos, colData, colStatus);

        btnPesquisar.setOnAction(e -> pesquisar(txtNome.getText().trim(), txtCpf.getText().trim()));

        btnExportar.setOnAction(e -> {
            Stage stage = (Stage) table.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar Receitas como CSV");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                ExportadorReceitas.exportarReceitasCSV(data, file.getAbsolutePath());
                showAlert("Exportação concluída para o arquivo: " + file.getAbsolutePath());
            }
        });

        btnValidar.setOnAction(e -> validarReceita());

        VBox centerContainer = new VBox(20, lblTitulo, gridFiltros, boxBotoes, table);
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

    private void pesquisar(String nome, String cpf) {
        List<Receita> filtered = receitaDAO.listarReceitas().stream()
                .filter(r -> (nome.isEmpty() || r.getPaciente().toLowerCase().contains(nome.toLowerCase()))
                        && (cpf.isEmpty() || (r.getCpf() != null && r.getCpf().startsWith(cpf))))
                .collect(Collectors.toList());
        data.setAll(filtered);
        table.setItems(data);
    }

    private void validarReceita() {
        Receita selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selecione uma receita para validar.");
            return;
        }
        if ("Validada".equalsIgnoreCase(selected.getStatus())) {
            showAlert("Essa receita já foi validada.");
            return;
        }
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Validar Receita");
        dialog.setHeaderText("Confirme a Validação");
        dialog.setContentText("Digite sua senha:");
        dialog.getEditor().setPromptText("Senha");
        dialog.getDialogPane().setPrefWidth(300);
        dialog.showAndWait().ifPresent(senha -> {
            if (senha.equals(Login.senhaLogada)) {
                selected.validar();
                receitaDAO.atualizarReceitas();
                showAlert("Receita validada com sucesso.");
                pesquisar("", "");
            } else {
                showAlert("Senha incorreta. Receita não validada.");
            }
        });
    }

    private Button criarBotaoMenu(String texto, Runnable acao) {
        Button btn = new Button(texto);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16px;");
        btn.setOnAction(e -> acao.run());
        return btn;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
