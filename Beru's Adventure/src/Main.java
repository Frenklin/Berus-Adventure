import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

public class Main extends Application {

    static Scene scene;
    static Group main_root = new Group();
    static Menu menu = new Menu();
    static ImageView picture_background;

    @Override
    public void start(Stage stage) {
        scene = new Scene(main_root, 1024, 639, Color.WHITE);
        stage.setTitle("Beru's Adventure");

        // Create login form
        GridPane loginForm = createLoginForm(stage);

        main_root.getChildren().add(loginForm);

        stage.setScene(scene);
        stage.show();
    }

    // Create login form
    private GridPane createLoginForm(Stage stage) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(240, 25, 25, 380));

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        // Username - Passw

        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameField, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);
        gridPane.add(loginButton, 1, 2);

        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Perform login authentication
            boolean isAuthenticated = authenticate(username, password);

            if (isAuthenticated) {
                // Clear login form
                gridPane.getChildren().clear();

                // Load the game
                menu.load(stage);
            } else {
                // Show error message or clear fields
                System.out.println("Invalid credentials. Please try again.");
                usernameField.clear();
                passwordField.clear();
            }
        });

        return gridPane;
    }

    // Perform authentication 
    private boolean authenticate(String username, String password) {

        return true;
    }

    // Function Main
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
