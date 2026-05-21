package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static Stage primaryStage;
    public static BorderPane rootLayout;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Marketplace App");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);

        rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: #ffffff;");

        rootLayout.setLeft(createSidebar());
        showDashboard();

        Scene scene = new Scene(rootLayout, 1000, 650);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // ── Sidebar ──────────────────────────────────────────────
    private VBox createSidebar() {
        VBox sidebar = new VBox();
        sidebar.setPrefWidth(200);
        sidebar.setStyle("-fx-background-color: #1a1a1a;");

        Label title = new Label("MARKETPLACE");
        title.setStyle(
                "-fx-text-fill: #ffffff;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 24 16 24 16;"
        );
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);

        javafx.scene.control.Separator sep =
                new javafx.scene.control.Separator();

        Button btnDashboard = navBtn("  Dashboard");
        Button btnVanzatori = navBtn("  Vânzători");
        Button btnProduse   = navBtn("  Produse");
        Button btnComenzi   = navBtn("  Comenzi");
        Button btnRapoarte  = navBtn("  Rapoarte");

        btnDashboard.setOnAction(e -> showDashboard());
        btnVanzatori.setOnAction(e -> rootLayout.setCenter(
                new VanzatoriController().getView()));
        btnProduse.setOnAction(e -> rootLayout.setCenter(
                new ProduseController().getView()));
        btnComenzi.setOnAction(e -> rootLayout.setCenter(
                new ComenziController().getView()));
        btnRapoarte.setOnAction(e -> rootLayout.setCenter(
                new RapoarteController().getView()));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label version = new Label("v1.0");
        version.setStyle(
                "-fx-text-fill: #555555;" +
                        "-fx-font-size: 11px;" +
                        "-fx-padding: 12 16 12 16;"
        );
        version.setMaxWidth(Double.MAX_VALUE);
        version.setAlignment(Pos.CENTER);

        sidebar.getChildren().addAll(
                title, sep,
                btnDashboard, btnVanzatori,
                btnProduse, btnComenzi, btnRapoarte,
                spacer, version
        );
        return sidebar;
    }

    // ── Buton navigare ───────────────────────────────────────
    public static Button navBtn(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(13, 16, 13, 20));
        btn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #aaaaaa;" +
                        "-fx-font-size: 13px;" +
                        "-fx-cursor: hand;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: #2d2d2d;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-font-size: 13px;" +
                        "-fx-cursor: hand;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #aaaaaa;" +
                        "-fx-font-size: 13px;" +
                        "-fx-cursor: hand;"
        ));
        return btn;
    }

    // ── Dashboard ────────────────────────────────────────────
    public static void showDashboard() {
        VBox view = new VBox(20);
        view.setPadding(new Insets(32));
        view.setStyle("-fx-background-color: #ffffff;");

        Label header = new Label("Dashboard");
        header.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #111111;"
        );

        Label sub = new Label("Bun venit în Marketplace App");
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: #888888;");

        HBox cards = new HBox(16);
        cards.getChildren().addAll(
                card("Vânzători", count("vanzatori")),
                card("Produse",   count("produse")),
                card("Comenzi",   count("comenzi"))
        );

        view.getChildren().addAll(header, sub, cards);
        rootLayout.setCenter(view);
    }

    // ── Card ─────────────────────────────────────────────────
    public static VBox card(String titlu, String valoare) {
        VBox c = new VBox(6);
        c.setPadding(new Insets(20));
        c.setPrefWidth(160);
        c.setStyle(
                "-fx-background-color: #f7f7f7;" +
                        "-fx-border-color: #dddddd;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-border-width: 1;"
        );
        Label t = new Label(titlu);
        t.setStyle("-fx-font-size: 12px; -fx-text-fill: #888888;");
        Label v = new Label(valoare);
        v.setStyle(
                "-fx-font-size: 26px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #111111;"
        );
        c.getChildren().addAll(t, v);
        return c;
    }

    // ── Count din BD ─────────────────────────────────────────
    public static String count(String tabela) {
        try {
            java.sql.ResultSet rs = dao.DatabaseConnection
                    .getInstance().getConnection()
                    .createStatement()
                    .executeQuery("SELECT COUNT(*) FROM " + tabela);
            if (rs.next()) return String.valueOf(rs.getInt(1));
        } catch (Exception e) { return "—"; }
        return "0";
    }
}