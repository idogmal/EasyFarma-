package view;

import dao.ReceitaDAO;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Receita;
import javafx.beans.property.SimpleStringProperty;
import util.ExportadorReceitas;
import javafx.stage.FileChooser;
import java.io.File;


import java.util.List;
import java.util.stream.Collectors;

public class PesquisarReceita extends Application {

    private TableView<Receita> table;
    private ObservableList<Receita> data;
    private ReceitaDAO receitaDAO = new ReceitaDAO();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Pesquisar Receita");

        // Campos de pesquisa
        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do Paciente");
        TextField txtCpf = new TextField();
        txtCpf.setPromptText("CPF do Paciente");

        Button btnPesquisar = new Button("Pesquisar");
        Button btnExportar = new Button("Exportar Receitas"); // Novo botão para exportação

        // Inicializa a tabela e a lista observável
        table = new TableView<>();
        data = FXCollections.observableArrayList();

        // Define as colunas da tabela
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

        // Evento de pesquisa
        btnPesquisar.setOnAction(e -> pesquisar(txtNome.getText().trim(), txtCpf.getText().trim()));

        // Evento do botão "Exportar Receitas"
        btnExportar.setOnAction(e -> {
            // Obtém a janela atual a partir da tabela
            Stage stage = (Stage) table.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar Receitas como CSV");
            // Adiciona filtro para arquivos CSV
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            // Abre o diálogo para salvar e obtém o arquivo selecionado
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                // Exporta as receitas para o caminho escolhido
                ExportadorReceitas.exportarReceitasCSV(data, file.getAbsolutePath());
                showAlert("Exportação concluída para o arquivo: " + file.getAbsolutePath());
            }
        });
        VBox root = new VBox(10, txtNome, txtCpf, btnPesquisar, btnExportar, table);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Filtra a lista de receitas com base no nome e CPF fornecidos.
     */
    private void pesquisar(String nome, String cpf) {
        List<Receita> filtered = receitaDAO.listarReceitas().stream()
                .filter(r -> (nome.isEmpty() || r.getPaciente().toLowerCase().contains(nome.toLowerCase())) &&
                        (cpf.isEmpty() || cpf.equals(r.getCpf())))
                .collect(Collectors.toList());
        data.setAll(filtered);
        table.setItems(data);
    }

    /**
     * Exibe um alerta com a mensagem informada.
     */
    private void showAlert(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, mensagem, ButtonType.OK);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
