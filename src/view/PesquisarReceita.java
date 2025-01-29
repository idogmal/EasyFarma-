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

import java.util.List;
import java.util.stream.Collectors;

public class PesquisarReceita extends Application {
    private TableView<Receita> tabelaReceitas;
    private ObservableList<Receita> listaReceitas;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Pesquisar Receita");

        // Campos de pesquisa
        TextField txtNomePaciente = new TextField();
        txtNomePaciente.setPromptText("Nome do Paciente");

        TextField txtCpfPaciente = new TextField();
        txtCpfPaciente.setPromptText("CPF do Paciente");

        Button btnPesquisar = new Button("Pesquisar");
        tabelaReceitas = new TableView<>();
        listaReceitas = FXCollections.observableArrayList();

        // Definir colunas da tabela
        TableColumn<Receita, String> colunaPaciente = new TableColumn<>("Paciente");
        colunaPaciente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaciente()));

        TableColumn<Receita, String> colunaCpf = new TableColumn<>("CPF");
        colunaCpf.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCpf())); // Ajustar se necessário

        TableColumn<Receita, String> colunaMedicamento = new TableColumn<>("Medicamentos");
        colunaMedicamento.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMedicamentosAsString()));

        TableColumn<Receita, String> colunaData = new TableColumn<>("Data da Prescrição");
        colunaData.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataPrescricao()));

        tabelaReceitas.getColumns().addAll(colunaPaciente, colunaCpf, colunaMedicamento, colunaData);

        // Evento de pesquisa
        btnPesquisar.setOnAction(e -> pesquisarReceitas(txtNomePaciente.getText().trim(), txtCpfPaciente.getText().trim()));

        // Layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(txtNomePaciente, txtCpfPaciente, btnPesquisar, tabelaReceitas);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void pesquisarReceitas(String nome, String cpf) {
        ReceitaDAO receitaDAO = new ReceitaDAO();
        List<Receita> receitasFiltradas = receitaDAO.listarReceitas().stream()
                .filter(r -> (nome.isEmpty() || r.getPaciente().toLowerCase().contains(nome.toLowerCase())) &&
                        (cpf.isEmpty() || r.getCpf().equals(cpf)))
                .collect(Collectors.toList());

        listaReceitas.setAll(receitasFiltradas);
        tabelaReceitas.setItems(listaReceitas);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
