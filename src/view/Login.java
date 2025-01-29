package view;

import controller.ReceitaController;
import dao.ReceitaDAO;
import dao.EstoqueDAO;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class Login extends Application {
    private final Map<String, String> usuarios;
    private final ReceitaController receitaController;

    public Login() {
        // Usuários fixos (para MVP)
        usuarios = new HashMap<>();
        usuarios.put("admin", "1234");
        usuarios.put("farmaceutico", "5678");

        // Criar instâncias do DAO e do Controller
        ReceitaDAO receitaDAO = new ReceitaDAO();
        EstoqueDAO estoqueDAO = new EstoqueDAO();
        receitaController = new ReceitaController(receitaDAO, estoqueDAO);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login");

        Label lblUsuario = new Label("Usuário:");
        TextField txtUsuario = new TextField();

        Label lblSenha = new Label("Senha:");
        PasswordField txtSenha = new PasswordField();

        Button btnLogin = new Button("Entrar");

        Label lblMensagem = new Label();

        // Evento de login
        btnLogin.setOnAction(e -> {
            String usuario = txtUsuario.getText().trim();
            String senha = txtSenha.getText().trim();

            if (usuarios.containsKey(usuario) && usuarios.get(usuario).equals(senha)) {
                lblMensagem.setText("Login bem-sucedido!");

                // Passando a instância correta de ReceitaController para o MenuPrincipal
                new MenuPrincipal(receitaController).start(new Stage());

                primaryStage.close(); // Fecha a tela de login
            } else {
                lblMensagem.setText("Usuário ou senha incorretos!");
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(lblUsuario, txtUsuario, lblSenha, txtSenha, btnLogin, lblMensagem);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
