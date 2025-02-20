package view;

import dao.UsuarioDAO;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;

public class CadastrarUsuario extends Application {
    private UsuarioDAO usuarioDAO;
    private Map<String, String> usuarios;

    // Construtor que recebe as dependências
    public CadastrarUsuario(UsuarioDAO usuarioDAO, Map<String, String> usuarios) {
        this.usuarioDAO = usuarioDAO;
        this.usuarios = usuarios;
    }

    // Construtor padrão (caso necessário)
    public CadastrarUsuario() {}

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cadastro de Usuário");

        Label lblNovoUsuario = new Label("Novo Usuário:");
        TextField txtNovoUsuario = new TextField();
        txtNovoUsuario.setPromptText("Digite o nome do usuário");

        Label lblNovaSenha = new Label("Nova Senha:");
        PasswordField txtNovaSenha = new PasswordField();
        txtNovaSenha.setPromptText("Digite a nova senha");

        Button btnCadastrar = new Button("Cadastrar");
        Label lblMensagem = new Label();

        // Evento para cadastrar o novo usuário
        btnCadastrar.setOnAction(e -> {
            String novoUsuario = txtNovoUsuario.getText().trim();
            String novaSenha = txtNovaSenha.getText().trim();

            if (novoUsuario.isEmpty() || novaSenha.isEmpty()) {
                lblMensagem.setText("Preencha todos os campos.");
            } else {
                // Adiciona o novo usuário ao mapa e persiste as alterações
                usuarios.put(novoUsuario, novaSenha);
                usuarioDAO.salvarUsuarios(usuarios);
                lblMensagem.setText("Usuário cadastrado com sucesso!");
                primaryStage.close();
            }
        });

        VBox layout = new VBox(10, lblNovoUsuario, txtNovoUsuario, lblNovaSenha, txtNovaSenha, btnCadastrar, lblMensagem);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
