package view;

import dao.ReceitaDAO;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
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
        primaryStage.setTitle("Pesquisar Receita");

        // Campos de pesquisa
        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do Paciente");
        TextField txtCpf = new TextField();
        txtCpf.setPromptText("CPF do Paciente");

        // Botões de ação
        Button btnPesquisar = new Button("Pesquisar");
        Button btnExportar = new Button("Exportar Receitas");
        Button btnValidar = new Button("Validar Receita");

        // Tabela para exibição das receitas
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

        // Ação do botão "Pesquisar": filtra receitas com base nos campos
        btnPesquisar.setOnAction(e -> pesquisar(txtNome.getText().trim(), txtCpf.getText().trim()));

        // Ação do botão "Exportar Receitas": abre um FileChooser para selecionar o local de salvamento
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

        // Ação do botão "Validar Receita": solicita senha do usuário logado para validar a receita selecionada
        btnValidar.setOnAction(e -> validarReceita());

        VBox root = new VBox(10, txtNome, txtCpf, btnPesquisar, btnExportar, btnValidar, table);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Filtra a lista de receitas com base no nome e CPF.
     * Para o CPF, é utilizada a verificação por prefixo.
     */
    private void pesquisar(String nome, String cpf) {
        List<Receita> filtered = receitaDAO.listarReceitas().stream()
                .filter(r -> (nome.isEmpty() || r.getPaciente().toLowerCase().contains(nome.toLowerCase()))
                        && (cpf.isEmpty() || (r.getCpf() != null && r.getCpf().startsWith(cpf))))
                .collect(Collectors.toList());
        data.setAll(filtered);
        table.setItems(data);
    }

    /**
     * Valida a receita selecionada, solicitando a senha do usuário logado.
     * Se a senha for correta (comparada com Login.senhaLogada), a receita é validada.
     */
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
            if (senha.equals(Login.senhaLogada)) {  // Certifique-se de que Login.senhaLogada esteja definido
                selected.validar();  // Atualiza o status para "Validada"
                receitaDAO.atualizarReceitas();
                showAlert("Receita validada com sucesso.");
                pesquisar("", ""); // Atualiza a tabela
            } else {
                showAlert("Senha incorreta. Receita não validada.");
            }
        });
    }

    /**
     * Exibe um alerta informativo com a mensagem fornecida.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
