package ui;

import dao.VanzatorDAO;
import model.Vanzator;
import util.Validator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.util.List;

public class VanzatoriController {

    private VanzatorDAO dao = new VanzatorDAO();
    private TableView<Vanzator> table = new TableView<>();
    private ObservableList<Vanzator> data = FXCollections.observableArrayList();

    private TextField txtNume    = new TextField();
    private TextField txtEmail   = new TextField();
    private TextField txtTelefon = new TextField();
    private TextField txtAdresa  = new TextField();
    private TextField txtCautare = new TextField();
    private Label lblStatus      = new Label();

    private Vanzator selected = null;

    // ── View principal ───────────────────────────────────────
    public VBox getView() {
        VBox view = new VBox(0);
        view.setStyle("-fx-background-color: #ffffff;");

        // Header
        HBox header = header("Vânzători");
        view.getChildren().add(header);

        // Continut
        HBox content = new HBox(0);
        content.getChildren().addAll(tablePanel(), formPanel());
        VBox.setVgrow(content, Priority.ALWAYS);
        view.getChildren().add(content);

        incarcaDate();
        return view;
    }

    // ── Header ───────────────────────────────────────────────
    private HBox header(String titlu) {
        HBox h = new HBox();
        h.setPadding(new Insets(20, 24, 16, 24));
        h.setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-border-color: #eeeeee;" +
                        "-fx-border-width: 0 0 1 0;"
        );
        Label lbl = new Label(titlu);
        lbl.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #111111;"
        );
        h.getChildren().add(lbl);
        return h;
    }

    // ── Tabel ────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    private VBox tablePanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(16, 12, 16, 24));
        VBox.setVgrow(panel, Priority.ALWAYS);
        HBox.setHgrow(panel, Priority.ALWAYS);

        // Bara cautare
        txtCautare.setPromptText("Caută după nume...");
        txtCautare.setStyle(fieldStyle());
        txtCautare.setPrefWidth(250);
        txtCautare.textProperty().addListener((obs, old, val) -> cauta(val));

        HBox searchBar = new HBox(8, new Label("🔍"), txtCautare);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        // Coloane tabel
        TableColumn<Vanzator, Integer> colId =
                new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(50);

        TableColumn<Vanzator, String> colNume =
                new TableColumn<>("Nume");
        colNume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        colNume.setPrefWidth(150);

        TableColumn<Vanzator, String> colEmail =
                new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmail.setPrefWidth(200);

        TableColumn<Vanzator, String> colTel =
                new TableColumn<>("Telefon");
        colTel.setCellValueFactory(new PropertyValueFactory<>("telefon"));
        colTel.setPrefWidth(130);

        table.getColumns().addAll(colId, colNume, colEmail, colTel);
        table.setItems(data);
        table.setStyle("-fx-font-size: 13px;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        // Click pe rand → incarca in formular
        table.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, val) -> {
                    if (val != null) incarcaInFormular(val);
                }
        );

        panel.getChildren().addAll(searchBar, table);
        return panel;
    }

    // ── Formular ─────────────────────────────────────────────
    private VBox formPanel() {
        VBox panel = new VBox(12);
        panel.setPadding(new Insets(16, 24, 16, 12));
        panel.setPrefWidth(280);
        panel.setStyle(
                "-fx-background-color: #fafafa;" +
                        "-fx-border-color: #eeeeee;" +
                        "-fx-border-width: 0 0 0 1;"
        );

        Label title = new Label("Detalii vânzător");
        title.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #333333;"
        );

        txtNume.setPromptText("Nume complet");
        txtEmail.setPromptText("Email");
        txtTelefon.setPromptText("Telefon");
        txtAdresa.setPromptText("Adresă");

        styleField(txtNume);
        styleField(txtEmail);
        styleField(txtTelefon);
        styleField(txtAdresa);

        lblStatus.setStyle("-fx-font-size: 12px;");
        lblStatus.setWrapText(true);

        Button btnAdauga    = actionBtn("Adaugă", "#1a1a1a", "#ffffff");
        Button btnActualiz  = actionBtn("Actualizează", "#444444", "#ffffff");
        Button btnSterge    = actionBtn("Șterge", "#cc0000", "#ffffff");
        Button btnNou       = actionBtn("Nou", "#888888", "#ffffff");

        btnAdauga.setOnAction(e -> adauga());
        btnActualiz.setOnAction(e -> actualizeaza());
        btnSterge.setOnAction(e -> sterge());
        btnNou.setOnAction(e -> reseteazaFormular());

        HBox btns1 = new HBox(8, btnAdauga, btnActualiz);
        HBox btns2 = new HBox(8, btnSterge, btnNou);

        panel.getChildren().addAll(
                title,
                lbl("Nume:"), txtNume,
                lbl("Email:"), txtEmail,
                lbl("Telefon:"), txtTelefon,
                lbl("Adresă:"), txtAdresa,
                lblStatus,
                btns1, btns2
        );
        return panel;
    }

    // ── Actiuni ──────────────────────────────────────────────
    private void adauga() {
        String err = Validator.valideazaVanzator(
                txtNume.getText(), txtEmail.getText(), txtTelefon.getText()
        );
        if (err != null) { status(err, true); return; }

        Vanzator v = new Vanzator(0,
                txtNume.getText().trim(),
                txtEmail.getText().trim(),
                txtTelefon.getText().trim()
        );
        v.setAdresa(txtAdresa.getText().trim());

        if (dao.adauga(v)) {
            status("✔ Vânzător adăugat!", false);
            incarcaDate();
            reseteazaFormular();
        } else {
            status("✘ Eroare la adăugare!", true);
        }
    }

    private void actualizeaza() {
        if (selected == null) { status("Selectează un vânzător!", true); return; }

        String err = Validator.valideazaVanzator(
                txtNume.getText(), txtEmail.getText(), txtTelefon.getText()
        );
        if (err != null) { status(err, true); return; }

        selected.setNume(txtNume.getText().trim());
        selected.setEmail(txtEmail.getText().trim());
        selected.setTelefon(txtTelefon.getText().trim());
        selected.setAdresa(txtAdresa.getText().trim());

        if (dao.actualizeaza(selected)) {
            status("✔ Actualizat!", false);
            incarcaDate();
        } else {
            status("✘ Eroare la actualizare!", true);
        }
    }

    private void sterge() {
        if (selected == null) { status("Selectează un vânzător!", true); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmare");
        confirm.setHeaderText("Ștergi vânzătorul: " + selected.getNume() + "?");
        confirm.setContentText("Această acțiune nu poate fi anulată.");

        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                if (dao.sterge(selected.getId())) {
                    status("✔ Șters!", false);
                    incarcaDate();
                    reseteazaFormular();
                } else {
                    status("✘ Eroare la ștergere!", true);
                }
            }
        });
    }

    private void cauta(String text) {
        if (text == null || text.trim().isEmpty()) {
            incarcaDate();
            return;
        }
        List<Vanzator> rezultat = dao.cautaDupaNume(text.trim());
        data.setAll(rezultat);
    }

    private void incarcaDate() {
        data.setAll(dao.getToti());
    }

    private void incarcaInFormular(Vanzator v) {
        selected = v;
        txtNume.setText(v.getNume());
        txtEmail.setText(v.getEmail());
        txtTelefon.setText(v.getTelefon());
        txtAdresa.setText(v.getAdresa() != null ? v.getAdresa() : "");
        lblStatus.setText("");
    }

    private void reseteazaFormular() {
        selected = null;
        txtNume.clear(); txtEmail.clear();
        txtTelefon.clear(); txtAdresa.clear();
        lblStatus.setText("");
        table.getSelectionModel().clearSelection();
    }

    // ── Helpers UI ───────────────────────────────────────────
    private void status(String msg, boolean eroare) {
        lblStatus.setText(msg);
        lblStatus.setStyle("-fx-font-size: 12px; -fx-text-fill: "
                + (eroare ? "#cc0000" : "#007700") + ";");
    }

    private Label lbl(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 12px; -fx-text-fill: #666666;");
        return l;
    }

    private void styleField(TextField f) {
        f.setStyle(fieldStyle());
        f.setMaxWidth(Double.MAX_VALUE);
    }

    private String fieldStyle() {
        return "-fx-background-color: #ffffff;" +
                "-fx-border-color: #dddddd;" +
                "-fx-border-radius: 4;" +
                "-fx-background-radius: 4;" +
                "-fx-padding: 7 10 7 10;" +
                "-fx-font-size: 13px;";
    }

    private Button actionBtn(String text, String bg, String fg) {
        Button btn = new Button(text);
        btn.setStyle(
                "-fx-background-color: " + bg + ";" +
                        "-fx-text-fill: " + fg + ";" +
                        "-fx-font-size: 12px;" +
                        "-fx-padding: 7 14 7 14;" +
                        "-fx-background-radius: 4;" +
                        "-fx-cursor: hand;"
        );
        HBox.setHgrow(btn, Priority.ALWAYS);
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }
}