import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Label label = new Label("JavaFX merge!");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 300, 200);
        stage.setTitle("daniel");
        stage.setScene(scene);
        stage.show();
    }
}