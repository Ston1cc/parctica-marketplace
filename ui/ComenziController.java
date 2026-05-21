package ui;

import dao.ComandaDAO;
import model.Comanda;
import model.enums.StatusComanda;
import util.Validator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

public class ComenziController {

    private ComandaDAO dao = new ComandaDAO();
    private TableView<Comanda> table = new TableView<>();
    private ObservableList<Comanda> data = FXCollections.observableArrayList();

    private TextField txtProdusId   = new TextField();
    private TextField txtVanzId     = new TextField();
    private TextField txtCantitate  = new TextField();
    private TextField txtPretUnit   = new TextField();
    private TextField txtNume       = new TextField();
    private TextField txtEmail      = new TextField();
    private TextField txtAdresa     = new TextField();
    private ComboBox<StatusComanda> cmbStatus = new ComboBox<>();
    private Label lblStatus         = new Label();
    private Comanda selected        = null;

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
        Label lbl = new Label("Comenzi");
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

        // Filtru status
        ComboBox<String> cmbFiltru = new ComboBox<>();
        cmbFiltru.getItems().addAll(
                "Toate", "IN_PROCESARE", "CONFIRMATA",
                "EXPEDIATA", "LIVRATA", "ANULATA"
        );
        cmbFiltru.setValue("Toate");
        cmbFiltru.setStyle(fs());
        cmbFiltru.setOnAction(e -> {
            String val = cmbFiltru.getValue();
            if ("Toate".equals(val)) incarcaDate();
            else data.setAll(dao.filtreazaDupaStatus(
                    StatusComanda.valueOf(val)));
        });

        HBox filtre = new HBox(8,
                new Label("Status:"), cmbFiltru
        );
        filtre.setAlignment(Pos.CENTER_LEFT);

        // Coloane
        TableColumn<Comanda, Integer> colId =
                new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(45);

        TableColumn<Comanda, String> colNume =
                new TableColumn<>("Cumpărător");
        colNume.setCellValueFactory(
                new PropertyValueFactory<>("numeCumparator"));
        colNume.setPrefWidth(150);

        TableColumn<Comanda, Integer> colCant =
                new TableColumn<>("Cant.");
        colCant.setCellValueFactory(
                new PropertyValueFactory<>("cantitate"));
        colCant.setPrefWidth(55);

        TableColumn<Comanda, Double> colTotal =
                new TableColumn<>("Total (lei)");
        colTotal.setCellValueFactory(
                new PropertyValueFactory<>("totalPret"));
        colTotal.setPrefWidth(100);

        TableColumn<Comanda, StatusComanda> colStatus =
                new TableColumn<>("Status");
        colStatus.setCellValueFactory(
                new PropertyValueFactory<>("status"));
        colStatus.setPrefWidth(120);

        TableColumn<Comanda, String> colData =
                new TableColumn<>("Data");
        colData.setCellValueFactory(
                new PropertyValueFactory<>("dataComenzii"));
        colData.setPrefWidth(100);

        table.getColumns().addAll(
                colId, colNume, colCant, colTotal, colStatus, colData);
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

        Label title = new Label("Detalii comandă");
        title.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #333333;"
        );

        cmbStatus.getItems().addAll(StatusComanda.values());
        cmbStatus.setValue(StatusComanda.IN_PROCESARE);
        cmbStatus.setMaxWidth(Double.MAX_VALUE);
        cmbStatus.setStyle(fs());

        for (TextField f : new TextField[]{
                txtProdusId, txtVanzId, txtCantitate,
                txtPretUnit, txtNume, txtEmail, txtAdresa
        }) {
            f.setStyle(fs());
            f.setMaxWidth(Double.MAX_VALUE);
        }

        txtProdusId.setPromptText("ID produs");
        txtVanzId.setPromptText("ID vânzător");
        txtCantitate.setPromptText("ex: 1");
        txtPretUnit.setPromptText("ex: 99.99");
        txtNume.setPromptText("Nume cumpărător");
        txtEmail.setPromptText("email@exemplu.com");
        txtAdresa.setPromptText("Adresă livrare");

        lblStatus.setStyle("-fx-font-size: 12px;");
        lblStatus.setWrapText(true);

        Button btnAdauga  = ab("Plasează",      "#1a1a1a", "#ffffff");
        Button btnUpdate  = ab("Schimbă status","#444444", "#ffffff");
        Button btnAnulez  = ab("Anulează",      "#cc0000", "#ffffff");
        Button btnNou     = ab("Nou",           "#888888", "#ffffff");

        btnAdauga.setOnAction(e -> adauga());
        btnUpdate.setOnAction(e -> schimbaStatus());
        btnAnulez.setOnAction(e -> anuleaza());
        btnNou.setOnAction(e -> resetForm());

        panel.getChildren().addAll(
                title,
                lbl("ID Produs:"), txtProdusId,
                lbl("ID Vânzător:"), txtVanzId,
                lbl("Cantitate:"), txtCantitate,
                lbl("Preț unitar:"), txtPretUnit,
                lbl("Cumpărător:"), txtNume,
                lbl("Email:"), txtEmail,
                lbl("Adresă livrare:"), txtAdresa,
                lbl("Status:"), cmbStatus,
                lblStatus,
                new HBox(8, btnAdauga, btnUpdate),
                new HBox(8, btnAnulez, btnNou)
        );
        return panel;
    }

    // ── Actiuni ──────────────────────────────────────────────
    private void adauga() {
        try {
            String err = Validator.valideazaComanda(
                    txtNume.getText(),
                    txtEmail.getText(),
                    Integer.parseInt(txtCantitate.getText())
            );
            if (err != null) { status(err, true); return; }

            int prodId  = Integer.parseInt(txtProdusId.getText());
            int vanzId  = Integer.parseInt(txtVanzId.getText());
            int cant    = Integer.parseInt(txtCantitate.getText());
            double pret = Double.parseDouble(txtPretUnit.getText());
            double total = pret * cant;

            Comanda c = new Comanda(0, prodId, vanzId, cant, pret, total);
            c.setNumeCumparator(txtNume.getText().trim());
            c.setEmailCumparator(txtEmail.getText().trim());
            c.setAdresaLivrare(txtAdresa.getText().trim());
            c.setStatus(cmbStatus.getValue());

            if (dao.adauga(c)) {
                status("✔ Comandă plasată! Total: " + total + " lei", false);
                incarcaDate(); resetForm();
            } else status("✘ Eroare la plasare!", true);

        } catch (NumberFormatException e) {
            status("✘ ID, cantitate și preț trebuie să fie numere!", true);
        }
    }

    private void schimbaStatus() {
        if (selected == null) { status("Selectează o comandă!", true); return; }
        if (dao.schimbaStatus(selected.getId(), cmbStatus.getValue())) {
            status("✔ Status schimbat!", false);
            incarcaDate();
        } else status("✘ Eroare!", true);
    }

    private void anuleaza() {
        if (selected == null) { status("Selectează o comandă!", true); return; }
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Confirmare");
        a.setHeaderText("Anulezi comanda #" + selected.getId() + "?");
        a.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                if (dao.anuleaza(selected.getId())) {
                    status("✔ Comandă anulată!", false);
                    incarcaDate(); resetForm();
                } else status("✘ Eroare!", true);
            }
        });
    }

    private void incarcaDate() { data.setAll(dao.getTote()); }

    private void incarcaInFormular(Comanda c) {
        selected = c;
        txtProdusId.setText(String.valueOf(c.getProdusId()));
        txtVanzId.setText(String.valueOf(c.getVanzatorId()));
        txtCantitate.setText(String.valueOf(c.getCantitate()));
        txtPretUnit.setText(String.valueOf(c.getPretUnitar()));
        txtNume.setText(c.getNumeCumparator() != null ? c.getNumeCumparator() : "");
        txtEmail.setText(c.getEmailCumparator() != null ? c.getEmailCumparator() : "");
        txtAdresa.setText(c.getAdresaLivrare() != null ? c.getAdresaLivrare() : "");
        cmbStatus.setValue(c.getStatus());
        lblStatus.setText("");
    }

    private void resetForm() {
        selected = null;
        txtProdusId.clear(); txtVanzId.clear();
        txtCantitate.clear(); txtPretUnit.clear();
        txtNume.clear(); txtEmail.clear(); txtAdresa.clear();
        cmbStatus.setValue(StatusComanda.IN_PROCESARE);
        lblStatus.setText("");
        table.getSelectionModel().clearSelection();
    }

    // ── Helpers ──────────────────────────────────────────────
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

    private Button ab(String t, String bg, String fg) {
        Button b = new Button(t);
        b.setStyle(
                "-fx-background-color:" + bg + ";" +
                        "-fx-text-fill:" + fg + ";" +
                        "-fx-font-size:12px;" +
                        "-fx-padding: 7 14 7 14;" +
                        "-fx-background-radius: 4;" +
                        "-fx-cursor: hand;"
        );
        HBox.setHgrow(b, Priority.ALWAYS);
        b.setMaxWidth(Double.MAX_VALUE);
        return b;
    }
}