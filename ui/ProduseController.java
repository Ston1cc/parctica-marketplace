package ui;

import dao.ProdusDAO;
import model.Produs;
import model.ProdusFizic;
import model.ProduDigital;
import util.Validator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.util.List;

public class ProduseController {

    private ProdusDAO dao = new ProdusDAO();
    private TableView<Produs> table = new TableView<>();
    private ObservableList<Produs> data = FXCollections.observableArrayList();

    private TextField txtNume      = new TextField();
    private TextField txtPret      = new TextField();
    private TextField txtStoc      = new TextField();
    private TextField txtDescriere = new TextField();
    private TextField txtExtra1    = new TextField();
    private TextField txtExtra2    = new TextField();
    private TextField txtVanzId    = new TextField();
    private TextField txtCautare   = new TextField();
    private TextField txtPretMax   = new TextField();
    private ComboBox<String> cmbTip = new ComboBox<>();
    private Label lblExtra1        = new Label("Greutate (kg):");
    private Label lblExtra2        = new Label("Dimensiuni:");
    private Label lblStatus        = new Label();
    private Produs selected        = null;

    // ── View ─────────────────────────────────────────────────
    public VBox getView() {
        VBox view = new VBox(0);
        view.setStyle("-fx-background-color: #ffffff;");
        view.getChildren().add(headerBar());

        HBox content = new HBox(0);
        content.getChildren().addAll(tablePanel(), formPanel());
        VBox.setVgrow(content, Priority.ALWAYS);
        view.getChildren().add(content);

        incarcaDate();
        return view;
    }

    // ── Header ───────────────────────────────────────────────
    private HBox headerBar() {
        HBox h = new HBox();
        h.setPadding(new Insets(20, 24, 16, 24));
        h.setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-border-color: #eeeeee;" +
                        "-fx-border-width: 0 0 1 0;"
        );
        Label lbl = new Label("Produse");
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
        HBox.setHgrow(panel, Priority.ALWAYS);
        VBox.setVgrow(panel, Priority.ALWAYS);

        // Filtre
        txtCautare.setPromptText("Caută după nume...");
        txtCautare.setStyle(fs());
        txtCautare.setPrefWidth(180);

        txtPretMax.setPromptText("Preț maxim...");
        txtPretMax.setStyle(fs());
        txtPretMax.setPrefWidth(120);

        cmbTip.getItems().addAll("Toate", "FIZIC", "DIGITAL");
        cmbTip.setValue("Toate");
        cmbTip.setStyle(fs());

        Button btnFiltreaza = new Button("Filtrează");
        btnFiltreaza.setStyle(btnStyle("#1a1a1a", "#ffffff"));
        btnFiltreaza.setOnAction(e -> filtreaza());

        Button btnReset = new Button("Reset");
        btnReset.setStyle(btnStyle("#888888", "#ffffff"));
        btnReset.setOnAction(e -> {
            txtCautare.clear();
            txtPretMax.clear();
            cmbTip.setValue("Toate");
            incarcaDate();
        });

        txtCautare.textProperty().addListener((o, ov, nv) -> filtreaza());

        HBox filtre = new HBox(8,
                new Label("🔍"), txtCautare,
                new Label("Preț max:"), txtPretMax,
                new Label("Tip:"), cmbTip,
                btnFiltreaza, btnReset
        );
        filtre.setAlignment(Pos.CENTER_LEFT);

        // Coloane
        TableColumn<Produs, Integer> colId   = col("ID",    "id",    50);
        TableColumn<Produs, String>  colNume = col("Nume",  "nume",  180);
        TableColumn<Produs, Double>  colPret = col("Preț",  "pret",  90);
        TableColumn<Produs, Integer> colStoc = col("Stoc",  "stoc",  70);

        TableColumn<Produs, String> colTip = new TableColumn<>("Tip");
        colTip.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getTipLivrare()
                )
        );
        colTip.setPrefWidth(200);

        table.getColumns().addAll(colId, colNume, colPret, colStoc, colTip);
        table.setItems(data);
        table.setStyle("-fx-font-size: 13px;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        table.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, val) -> { if (val != null) incarcaInFormular(val); }
        );

        panel.getChildren().addAll(filtre, table);
        return panel;
    }

    // ── Formular ─────────────────────────────────────────────
    private VBox formPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(16, 24, 16, 12));
        panel.setPrefWidth(290);
        panel.setStyle(
                "-fx-background-color: #fafafa;" +
                        "-fx-border-color: #eeeeee;" +
                        "-fx-border-width: 0 0 0 1;"
        );

        Label title = new Label("Detalii produs");
        title.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #333333;"
        );

        cmbTip.getItems().clear();
        cmbTip.getItems().addAll("FIZIC", "DIGITAL");
        cmbTip.setValue("FIZIC");
        cmbTip.setMaxWidth(Double.MAX_VALUE);
        cmbTip.setOnAction(e -> updateExtraFields());

        txtNume.setPromptText("Nume produs");
        txtPret.setPromptText("0.00");
        txtStoc.setPromptText("0");
        txtDescriere.setPromptText("Descriere");
        txtExtra1.setPromptText("ex: 0.5");
        txtExtra2.setPromptText("ex: 30x20x10");
        txtVanzId.setPromptText("ID vânzător");

        for (TextField f : new TextField[]{
                txtNume, txtPret, txtStoc, txtDescriere,
                txtExtra1, txtExtra2, txtVanzId
        }) {
            f.setStyle(fs());
            f.setMaxWidth(Double.MAX_VALUE);
        }

        lblStatus.setStyle("-fx-font-size: 12px;");
        lblStatus.setWrapText(true);

        Button btnAdauga   = ab("Adaugă",       "#1a1a1a", "#ffffff");
        Button btnUpdate   = ab("Actualizează", "#444444", "#ffffff");
        Button btnSterge   = ab("Șterge",       "#cc0000", "#ffffff");
        Button btnNou      = ab("Nou",           "#888888", "#ffffff");

        btnAdauga.setOnAction(e -> adauga());
        btnUpdate.setOnAction(e -> actualizeaza());
        btnSterge.setOnAction(e -> sterge());
        btnNou.setOnAction(e -> resetForm());

        panel.getChildren().addAll(
                title,
                lbl("Tip:"), cmbTip,
                lbl("Nume:"), txtNume,
                lbl("Preț (lei):"), txtPret,
                lbl("Stoc:"), txtStoc,
                lbl("Descriere:"), txtDescriere,
                lblExtra1, txtExtra1,
                lblExtra2, txtExtra2,
                lbl("ID Vânzător:"), txtVanzId,
                lblStatus,
                new HBox(8, btnAdauga, btnUpdate),
                new HBox(8, btnSterge, btnNou)
        );
        return panel;
    }

    // ── Actiuni ──────────────────────────────────────────────
    private void adauga() {
        try {
            String err = Validator.valideazaProdus(
                    txtNume.getText(),
                    Double.parseDouble(txtPret.getText()),
                    Integer.parseInt(txtStoc.getText())
            );
            if (err != null) { status(err, true); return; }

            Produs p;
            int vId = Integer.parseInt(txtVanzId.getText());

            if ("FIZIC".equals(cmbTip.getValue())) {
                p = new ProdusFizic(0, txtNume.getText().trim(),
                        Double.parseDouble(txtPret.getText()),
                        Integer.parseInt(txtStoc.getText()), vId,
                        Double.parseDouble(txtExtra1.getText()),
                        txtExtra2.getText().trim());
            } else {
                p = new ProduDigital(0, txtNume.getText().trim(),
                        Double.parseDouble(txtPret.getText()),
                        Integer.parseInt(txtStoc.getText()), vId,
                        txtExtra1.getText().trim(),
                        txtExtra2.getText().trim());
            }
            p.setDescriere(txtDescriere.getText().trim());

            if (dao.adauga(p)) {
                status("✔ Produs adăugat!", false);
                incarcaDate(); resetForm();
            } else status("✘ Eroare la adăugare!", true);

        } catch (NumberFormatException e) {
            status("✘ Preț, stoc și ID trebuie să fie numere!", true);
        }
    }

    private void actualizeaza() {
        if (selected == null) { status("Selectează un produs!", true); return; }
        try {
            selected.setNume(txtNume.getText().trim());
            selected.setPret(Double.parseDouble(txtPret.getText()));
            selected.setStoc(Integer.parseInt(txtStoc.getText()));
            selected.setDescriere(txtDescriere.getText().trim());

            if (dao.actualizeaza(selected)) {
                status("✔ Actualizat!", false);
                incarcaDate();
            } else status("✘ Eroare!", true);

        } catch (NumberFormatException e) {
            status("✘ Preț și stoc trebuie să fie numere!", true);
        }
    }

    private void sterge() {
        if (selected == null) { status("Selectează un produs!", true); return; }
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Confirmare");
        a.setHeaderText("Ștergi produsul: " + selected.getNume() + "?");
        a.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                if (dao.sterge(selected.getId())) {
                    status("✔ Șters!", false);
                    incarcaDate(); resetForm();
                } else status("✘ Eroare!", true);
            }
        });
    }

    private void filtreaza() {
        String tip = cmbTip.getValue();
        String nume = txtCautare.getText().trim();
        String pretMaxStr = txtPretMax.getText().trim();

        List<Produs> lista;

        if (!nume.isEmpty()) {
            lista = dao.cautaDupaNume(nume);
        } else if (!pretMaxStr.isEmpty()) {
            try {
                lista = dao.filtreazaDupaPret(Double.parseDouble(pretMaxStr));
            } catch (NumberFormatException e) {
                lista = dao.getToti();
            }
        } else if (tip != null && !tip.equals("Toate")) {
            lista = dao.filtreazaDupaTip(tip);
        } else {
            lista = dao.getToti();
        }
        data.setAll(lista);
    }

    private void incarcaDate() { data.setAll(dao.getToti()); }

    private void incarcaInFormular(Produs p) {
        selected = p;
        txtNume.setText(p.getNume());
        txtPret.setText(String.valueOf(p.getPret()));
        txtStoc.setText(String.valueOf(p.getStoc()));
        txtDescriere.setText(p.getDescriere() != null ? p.getDescriere() : "");
        txtVanzId.setText(String.valueOf(p.getVanzatorId()));

        if (p instanceof ProdusFizic) {
            cmbTip.setValue("FIZIC");
            txtExtra1.setText(String.valueOf(((ProdusFizic) p).getGreutateKg()));
            txtExtra2.setText(((ProdusFizic) p).getDimensiuni());
        } else if (p instanceof ProduDigital) {
            cmbTip.setValue("DIGITAL");
            txtExtra1.setText(((ProduDigital) p).getLinkDescarcare());
            txtExtra2.setText(((ProduDigital) p).getFormat());
        }
        updateExtraFields();
        lblStatus.setText("");
    }

    private void updateExtraFields() {
        if ("FIZIC".equals(cmbTip.getValue())) {
            lblExtra1.setText("Greutate (kg):");
            txtExtra1.setPromptText("ex: 0.5");
            lblExtra2.setText("Dimensiuni:");
            txtExtra2.setPromptText("ex: 30x20x10");
        } else {
            lblExtra1.setText("Link descărcare:");
            txtExtra1.setPromptText("https://...");
            lblExtra2.setText("Format fișier:");
            txtExtra2.setPromptText("ZIP / PDF / MP3");
        }
    }

    private void resetForm() {
        selected = null;
        txtNume.clear(); txtPret.clear(); txtStoc.clear();
        txtDescriere.clear(); txtExtra1.clear();
        txtExtra2.clear(); txtVanzId.clear();
        lblStatus.setText("");
        table.getSelectionModel().clearSelection();
    }

    // ── Helpers UI ───────────────────────────────────────────
    private void status(String msg, boolean err) {
        lblStatus.setText(msg);
        lblStatus.setStyle("-fx-font-size: 12px; -fx-text-fill: "
                + (err ? "#cc0000" : "#007700") + ";");
    }

    private Label lbl(String t) {
        Label l = new Label(t);
        l.setStyle("-fx-font-size: 12px; -fx-text-fill: #666666;");
        return l;
    }

    private String fs() {
        return "-fx-background-color: #ffffff;" +
                "-fx-border-color: #dddddd;" +
                "-fx-border-radius: 4;" +
                "-fx-background-radius: 4;" +
                "-fx-padding: 7 10 7 10;" +
                "-fx-font-size: 13px;";
    }

    private String btnStyle(String bg, String fg) {
        return "-fx-background-color:" + bg + ";" +
                "-fx-text-fill:" + fg + ";" +
                "-fx-font-size:12px;" +
                "-fx-padding: 7 14 7 14;" +
                "-fx-background-radius: 4;" +
                "-fx-cursor: hand;";
    }

    private Button ab(String t, String bg, String fg) {
        Button b = new Button(t);
        b.setStyle(btnStyle(bg, fg));
        HBox.setHgrow(b, Priority.ALWAYS);
        b.setMaxWidth(Double.MAX_VALUE);
        return b;
    }

    @SuppressWarnings("unchecked")
    private <T> TableColumn<Produs, T> col(String title, String prop, int w) {
        TableColumn<Produs, T> c = new TableColumn<>(title);
        c.setCellValueFactory(new PropertyValueFactory<>(prop));
        c.setPrefWidth(w);
        return c;
    }
}