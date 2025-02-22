package view;

import controller.ReceitaController;
import dao.EstoqueDAO;
import dao.ReceitaDAO;
import dao.UsuarioDAO;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
        primaryStage.setTitle("EasyFarma - Login");

        // ===================== TOPO: LOGO CENTRALIZADA =====================
        // Cria um ImageView para a logo
        ImageView logoView = null;
        try {
            Image logo = new Image(getClass().getResourceAsStream("/logo.png"));
            logoView = new ImageView(logo);
            logoView.setFitHeight(100); // Aumente conforme necessário
            logoView.setPreserveRatio(true);
        } catch(Exception ex) {
            System.err.println("Logo não encontrada!");
        }
        // Coloca a logo em um HBox centralizado
        HBox topBox = new HBox();
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(20, 0, 20, 0));
        if (logoView != null) {
            topBox.getChildren().add(logoView);
        }

        // ===================== FORMULÁRIO CENTRAL =====================
        // Criação dos campos e botões do formulário
        Label lblUsuario = new Label("Usuário:");
        lblUsuario.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Digite o usuário");

        Label lblSenha = new Label("Senha:");
        lblSenha.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        PasswordField txtSenha = new PasswordField();
        txtSenha.setPromptText("Digite a senha");

        Button btnLogin = new Button("Entrar");
        btnLogin.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        btnLogin.setPrefWidth(100);
        Button btnCadastrar = new Button("Cadastrar");
        btnCadastrar.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-weight: bold;");
        btnCadastrar.setPrefWidth(100);

        // Label de mensagem para feedback
        Label lblMensagem = new Label();

        // Formulário central em VBox
        VBox formBox = new VBox(10);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(20));
        formBox.getChildren().addAll(lblUsuario, txtUsuario, lblSenha, txtSenha, btnLogin, btnCadastrar, lblMensagem);

        // ===================== AÇÕES DOS BOTÕES =====================
        btnLogin.setOnAction(e -> {
            String usuario = txtUsuario.getText().trim();
            String senha = txtSenha.getText().trim();
            if (usuarios.containsKey(usuario) && usuarios.get(usuario).equals(senha)) {
                // Armazena os dados do usuário logado
                Login.usuarioLogado = usuario;
                Login.senhaLogada = senha;
                lblMensagem.setText("Login bem-sucedido!");
                // Abre diretamente a tela de Cadastrar Receita
                new CadastrarReceita(receitaController).start(new Stage());
                primaryStage.close();
            } else {
                lblMensagem.setText("Usuário ou senha incorretos!");
            }
        });

        btnCadastrar.setOnAction(e -> {
            new CadastrarUsuario(usuarioDAO, usuarios).start(new Stage());
        });

        // ===================== LAYOUT PRINCIPAL (BorderPane) =====================
        BorderPane root = new BorderPane();
        root.setTop(topBox);
        root.setCenter(formBox);
        // Se desejar, você pode definir um fundo, por exemplo, branco:
        root.setStyle("-fx-background-color: white;");

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
