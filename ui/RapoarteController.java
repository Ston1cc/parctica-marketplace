package ui;

import util.RaportService;
import util.ExportService;
import dao.VanzatorDAO;
import dao.ProdusDAO;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public class RapoarteController {

    private RaportService raportService  = new RaportService();
    private ExportService exportService  = new ExportService();
    private VanzatorDAO   vanzatorDAO    = new VanzatorDAO();
    private ProdusDAO     produsDAO      = new ProdusDAO();
    private TextArea      txtOutput      = new TextArea();

    // ── View ─────────────────────────────────────────────────
    public VBox getView() {
        VBox view = new VBox(0);
        view.setStyle("-fx-background-color: #ffffff;");

        // Header
        HBox header = new HBox();
        header.setPadding(new Insets(20, 24, 16, 24));
        header.setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-border-color: #eeeeee;" +
                        "-fx-border-width: 0 0 1 0;"
        );
        Label title = new Label("Rapoarte & Export");
        title.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #111111;"
        );
        header.getChildren().add(title);

        // Continut
        HBox content = new HBox(0);
        content.getChildren().addAll(outputPanel(), buttonsPanel());
        VBox.setVgrow(content, Priority.ALWAYS);

        view.getChildren().addAll(header, content);
        return view;
    }

    // ── Panel output ─────────────────────────────────────────
    private VBox outputPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(16, 12, 16, 24));
        HBox.setHgrow(panel, Priority.ALWAYS);
        VBox.setVgrow(panel, Priority.ALWAYS);

        Label lbl = new Label("Output raport:");
        lbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #666666;");

        txtOutput.setStyle(
                "-fx-font-family: monospace;" +
                        "-fx-font-size: 12px;" +
                        "-fx-background-color: #f7f7f7;" +
                        "-fx-border-color: #dddddd;" +
                        "-fx-border-radius: 4;"
        );
        txtOutput.setEditable(false);
        txtOutput.setWrapText(false);
        VBox.setVgrow(txtOutput, Priority.ALWAYS);

        panel.getChildren().addAll(lbl, txtOutput);
        return panel;
    }

    // ── Panel butoane ────────────────────────────────────────
    private VBox buttonsPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(16, 24, 16, 12));
        panel.setPrefWidth(220);
        panel.setStyle(
                "-fx-background-color: #fafafa;" +
                        "-fx-border-color: #eeeeee;" +
                        "-fx-border-width: 0 0 0 1;"
        );

        Label lblRap = new Label("RAPOARTE");
        lblRap.setStyle(
                "-fx-font-size: 11px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #888888;"
        );

        Button btnR1 = btn("Top Vânzători");
        Button btnR2 = btn("Comenzi pe Status");
        Button btnR3 = btn("Stoc Scăzut");
        Button btnAll = btn("Toate Rapoartele");

        btnR1.setOnAction(e -> afiseaza(raportService.raportTopVanzatori()));
        btnR2.setOnAction(e -> afiseaza(raportService.raportComenziPeStatus()));
        btnR3.setOnAction(e -> afiseaza(raportService.raportStocScazut()));
        btnAll.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            for (String l : raportService.raportTopVanzatori())    sb.append(l).append("\n");
            sb.append("\n");
            for (String l : raportService.raportComenziPeStatus()) sb.append(l).append("\n");
            sb.append("\n");
            for (String l : raportService.raportStocScazut())      sb.append(l).append("\n");
            txtOutput.setText(sb.toString());
        });

        Separator sep = new Separator();

        Label lblExp = new Label("EXPORT");
        lblExp.setStyle(
                "-fx-font-size: 11px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #888888;"
        );

        Button btnExpVanz  = btn("Export Vânzători CSV");
        Button btnExpProd  = btn("Export Produse CSV");
        Button btnExpCom   = btn("Export Comenzi CSV");
        Button btnExpRap   = btn("Export Raport TXT");

        btnExpVanz.setOnAction(e -> {
            File f = chooser("vanzatori.csv", "*.csv");
            if (f != null) {
                exportService.exportVanzatoriCSV(vanzatorDAO.getToti(), f.getAbsolutePath());
                txtOutput.setText("✔ Export vânzători salvat:\n" + f.getAbsolutePath());
            }
        });

        btnExpProd.setOnAction(e -> {
            File f = chooser("produse.csv", "*.csv");
            if (f != null) {
                exportService.exportProduseCSV(produsDAO.getToti(), f.getAbsolutePath());
                txtOutput.setText("✔ Export produse salvat:\n" + f.getAbsolutePath());
            }
        });

        btnExpCom.setOnAction(e -> {
            File f = chooser("comenzi.csv", "*.csv");
            if (f != null) {
                exportService.exportComenziCSV(f.getAbsolutePath());
                txtOutput.setText("✔ Export comenzi salvat:\n" + f.getAbsolutePath());
            }
        });

        btnExpRap.setOnAction(e -> {
            File f = chooser("raport.txt", "*.txt");
            if (f != null) {
                List<String> linii = raportService.raportTopVanzatori();
                linii.addAll(raportService.raportComenziPeStatus());
                linii.addAll(raportService.raportStocScazut());
                exportService.exportRaportTXT(linii, f.getAbsolutePath());
                txtOutput.setText("✔ Raport TXT salvat:\n" + f.getAbsolutePath());
            }
        });

        panel.getChildren().addAll(
                lblRap,
                btnR1, btnR2, btnR3, btnAll,
                sep,
                lblExp,
                btnExpVanz, btnExpProd,
                btnExpCom, btnExpRap
        );
        return panel;
    }

    // ── Helpers ──────────────────────────────────────────────
    private void afiseaza(List<String> linii) {
        StringBuilder sb = new StringBuilder();
        for (String l : linii) sb.append(l).append("\n");
        txtOutput.setText(sb.toString());
    }

    private Button btn(String text) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setStyle(
                "-fx-background-color: #1a1a1a;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-font-size: 12px;" +
                        "-fx-padding: 8 12 8 12;" +
                        "-fx-background-radius: 4;" +
                        "-fx-cursor: hand;"
        );
        b.setOnMouseEntered(e -> b.setStyle(
                "-fx-background-color: #333333;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-font-size: 12px;" +
                        "-fx-padding: 8 12 8 12;" +
                        "-fx-background-radius: 4;" +
                        "-fx-cursor: hand;"
        ));
        b.setOnMouseExited(e -> b.setStyle(
                "-fx-background-color: #1a1a1a;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-font-size: 12px;" +
                        "-fx-padding: 8 12 8 12;" +
                        "-fx-background-radius: 4;" +
                        "-fx-cursor: hand;"
        ));
        return b;
    }

    private File chooser(String defaultName, String ext) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Salvează fișierul");
        fc.setInitialFileName(defaultName);
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fișiere", ext)
        );
        return fc.showSaveDialog(MainApp.primaryStage);
    }
}