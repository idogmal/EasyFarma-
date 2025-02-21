package view;

import controller.ReceitaController;
import dao.EstoqueDAO;
import dao.ReceitaDAO;
import dao.UsuarioDAO;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;

public class Login extends Application {
    // Variáveis estáticas para armazenar os dados do usuário logado
    public static String usuarioLogado;
    public static String senhaLogada;

    // Mapa de usuários persistentes
    private Map<String, String> usuarios;
    private final ReceitaController receitaController;
    private UsuarioDAO usuarioDAO;

    public Login() {
        usuarioDAO = new UsuarioDAO();
        // Carrega os usuários do arquivo JSON
        usuarios = usuarioDAO.carregarUsuarios();
        // Se estiver vazio, adiciona os usuários fixos e salva
        if (usuarios.isEmpty()) {
            usuarios.put("admin", "1234");
            usuarios.put("farmaceutico", "5678");
            usuarioDAO.salvarUsuarios(usuarios);
        }
        // Cria instâncias do DAO e do Controller para o restante do sistema
        ReceitaDAO receitaDAO = new ReceitaDAO();
        EstoqueDAO estoqueDAO = new EstoqueDAO();
        receitaController = new ReceitaController(receitaDAO, estoqueDAO);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login");

        Label lblUsuario = new Label("Usuário:");
        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Digite o usuário");

        Label lblSenha = new Label("Senha:");
        PasswordField txtSenha = new PasswordField();
        txtSenha.setPromptText("Digite a senha");

        Button btnLogin = new Button("Entrar");
        Button btnCadastrar = new Button("Cadastrar"); // Botão para abrir a tela de cadastro

        Label lblMensagem = new Label();

        // Evento de login
        btnLogin.setOnAction(e -> {
            String usuario = txtUsuario.getText().trim();
            String senha = txtSenha.getText().trim();

            if (usuarios.containsKey(usuario) && usuarios.get(usuario).equals(senha)) {
                // Armazena os dados do usuário logado
                Login.usuarioLogado = usuario;
                Login.senhaLogada = senha;

                lblMensagem.setText("Login bem-sucedido!");
                new MenuPrincipal(receitaController).start(new Stage());
                primaryStage.close();
            } else {
                lblMensagem.setText("Usuário ou senha incorretos!");
            }
        });

        // Evento do botão "Cadastrar"
        btnCadastrar.setOnAction(e -> {
            // Abre a tela de cadastro e passa a instância do UsuarioDAO e o mapa de usuários
            new CadastrarUsuario(usuarioDAO, usuarios).start(new Stage());
        });

        VBox layout = new VBox(10, lblUsuario, txtUsuario, lblSenha, txtSenha, btnLogin, btnCadastrar, lblMensagem);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
