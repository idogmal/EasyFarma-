package view;

import controller.ReceitaController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuPrincipal extends Application {
    private final ReceitaController receitaController;

    public MenuPrincipal(ReceitaController receitaController) {
        this.receitaController = receitaController;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Menu Principal");

        // Botões do menu
        Button btnCadastrarReceita = new Button("Cadastrar Receita");
        Button btnPesquisarReceita = new Button("Pesquisar Receita");
        Button btnVisualizarEstoque = new Button("Visualizar Estoque");
        Button btnSair = new Button("Sair");

        // Estilo dos botões
        btnCadastrarReceita.setPrefWidth(200);
        btnPesquisarReceita.setPrefWidth(200);
        btnVisualizarEstoque.setPrefWidth(200);
        btnSair.setPrefWidth(200);

        // Eventos dos botões
        btnCadastrarReceita.setOnAction(e -> new CadastrarReceita(receitaController).start(new Stage()));
        btnPesquisarReceita.setOnAction(e -> new PesquisarReceita().start(new Stage())); // Corrigido: Removido o argumento
        btnVisualizarEstoque.setOnAction(e -> visualizarEstoque());
        btnSair.setOnAction(e -> primaryStage.close());

        // Layout
        VBox layout = new VBox(15);
        layout.getChildren().addAll(btnCadastrarReceita, btnPesquisarReceita, btnVisualizarEstoque, btnSair);
        layout.setAlignment(Pos.CENTER);

        // Cena
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void visualizarEstoque() {
        System.out.println("Função de visualizar estoque ainda não implementada.");
    }
}
