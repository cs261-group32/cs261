package com.group32.cs261project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class ConfigurePage implements Page {

    private final App app;
    private final SimulationService sim;

    private final BorderPane root = new BorderPane();

    // sliders
    private final Slider runwaySlider = new Slider(1, 10, 2);
    private final Slider inboundSlider = new Slider(0, 30, 30);
    private final Slider outboundSlider = new Slider(0, 30, 30);

    private final Label runwayValue = new Label();
    private final Label inboundValue = new Label();
    private final Label outboundValue = new Label();

    private final Label errorLabel = new Label();

    private final GridPane runwayGrid = new GridPane();
    private final List<RunwayCard> runwayCards = new ArrayList<>();

    private SimulationData data = new SimulationData();

    // ---------- styling helpers ----------
    private static final String PAGE_BG = "-fx-background-color: white;";
    private static final String PANEL =
            "-fx-background-color: white;" +
            "-fx-border-color: #111;" +
            "-fx-border-width: 1;" +
            "-fx-padding: 16;";
    private static final String CARD =
            "-fx-background-color: white;" +
            "-fx-border-color: #111;" +
            "-fx-border-width: 1;" +
            "-fx-padding: 14;";
    private static final String CARD_DISABLED =
            "-fx-background-color: #e9ecef;" +
            "-fx-border-color: #111;" +
            "-fx-border-width: 1;" +
            "-fx-padding: 14;";
    private static final String BTN_PRIMARY =
            "-fx-background-color: #111; -fx-text-fill: white; -fx-font-weight: 700; -fx-padding: 10 14;";
    private static final String BTN_DANGER =
            "-fx-background-color: #f48484; -fx-text-fill: white; -fx-font-weight: 700; -fx-padding: 10 14;";
    private static final String BTN_SUCCESS =
            "-fx-background-color: #0aa84f; -fx-text-fill: white; -fx-font-weight: 700; -fx-padding: 10 14;";
    private static final String BTN_SOFT =
            "-fx-background-color: #8fb0ff; -fx-text-fill: white; -fx-font-weight: 700; -fx-padding: 8 12;";
    private static final String BTN_SOFT_2 =
            "-fx-background-color: #5f87ff; -fx-text-fill: white; -fx-font-weight: 700; -fx-padding: 8 12;";
    private static final String BTN_SOFT_3 =
            "-fx-background-color: #2f6dff; -fx-text-fill: white; -fx-font-weight: 700; -fx-padding: 8 12;";

    public ConfigurePage(App app, SimulationService sim) {
        this.app = app;
        this.sim = sim;

        root.setStyle(PAGE_BG);
        root.setPadding(new Insets(16));

        // ---------- Header (title + subtitle) ----------
        VBox header = new VBox(4);
        Label title = new Label("✈ Airport Traffic Simulation System");
        title.setFont(Font.font(26));
        Label subtitle = new Label("model aircraft throughput and analyse runway configurations");
        subtitle.setStyle("-fx-text-fill: #444;");

        header.getChildren().addAll(title, subtitle);
        header.setPadding(new Insets(0, 0, 12, 0));
        root.setTop(header);

        // ---------- Main split: left sidebar + right content ----------
        HBox main = new HBox(18);
        main.setPadding(new Insets(8, 0, 0, 0));

        VBox leftSidebar = buildLeftSidebar();
        VBox rightContent = buildRightContent();

        HBox.setHgrow(rightContent, Priority.ALWAYS);

        main.getChildren().addAll(leftSidebar, rightContent);
        root.setCenter(main);

        // initial values
        initSliders();
        loadIntoControls(data);
        buildRunwayCards();
        refreshRunwayGrid();

        // listeners
        runwaySlider.valueProperty().addListener((obs, o, n) -> {
            data.runwayCount = (int) Math.round(n.doubleValue());
            runwayValue.setText(String.valueOf(data.runwayCount));
            data.ensureRunwayListSize();
            refreshRunwayGrid();
        });

        inboundSlider.valueProperty().addListener((obs, o, n) -> {
            data.inboundRatePerHour = (int) Math.round(n.doubleValue());
            inboundValue.setText(String.valueOf(data.inboundRatePerHour));
        });

        outboundSlider.valueProperty().addListener((obs, o, n) -> {
            data.outboundRatePerHour = (int) Math.round(n.doubleValue());
            outboundValue.setText(String.valueOf(data.outboundRatePerHour));
        });
    }

    @Override
    public Parent getView() {
        return root;
    }

    @Override
    public void onEnter(Object ignored) {
        // keep last state
    }

    // ---------------- UI builders ----------------

    private VBox buildLeftSidebar() {
        VBox left = new VBox(16);
        left.setPrefWidth(380);

        // Simulation Control panel
        VBox simPanel = new VBox(14);
        simPanel.setStyle(PANEL);

        Label simTitle = new Label("Simulation Control");
        simTitle.setFont(Font.font(20));

        // Number of runways
        VBox runwayRow = new VBox(6);
        Label runwayLabel = new Label("Number of Runways: ");
        HBox runwayLine = new HBox(6, runwayLabel, runwayValue);
        runwayLine.setAlignment(Pos.CENTER_LEFT);
        runwayRow.getChildren().addAll(runwayLine, runwaySlider);

        Button runBtn = new Button("▶  Run Simulation");
        runBtn.setMaxWidth(Double.MAX_VALUE);
        runBtn.setStyle(BTN_PRIMARY);
        runBtn.setOnAction(e -> onRun());

        // (NO end simulation button)

        Button saveBtn = new Button("💾  Save Simulation");
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setStyle(BTN_SUCCESS);
        // stub: you can wire this later
        saveBtn.setOnAction(e -> errorLabel.setText("Save not implemented yet."));

        HBox undoRedoReset = new HBox(10);
        Button undoBtn = new Button("↶  Undo");
        Button redoBtn = new Button("↷  Redo");
        Button resetBtn = new Button("⟲  Reset");

        undoBtn.setStyle(BTN_SOFT);
        redoBtn.setStyle(BTN_SOFT_2);
        resetBtn.setStyle(BTN_SOFT_3);

        undoBtn.setOnAction(e -> errorLabel.setText("Undo not implemented yet."));
        redoBtn.setOnAction(e -> errorLabel.setText("Redo not implemented yet."));
        resetBtn.setOnAction(e -> {
            data = new SimulationData();
            loadIntoControls(data);
            buildRunwayCards();
            refreshRunwayGrid();
            errorLabel.setText("");
        });

        undoBtn.setMaxWidth(Double.MAX_VALUE);
        redoBtn.setMaxWidth(Double.MAX_VALUE);
        resetBtn.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(undoBtn, Priority.ALWAYS);
        HBox.setHgrow(redoBtn, Priority.ALWAYS);
        HBox.setHgrow(resetBtn, Priority.ALWAYS);

        undoRedoReset.getChildren().addAll(undoBtn, redoBtn, resetBtn);

        simPanel.getChildren().addAll(simTitle, runwayRow, runBtn, saveBtn, undoRedoReset);

        // Aircraft Generation panel
        VBox aircraftPanel = new VBox(14);
        aircraftPanel.setStyle(PANEL);

        Label aircraftTitle = new Label("Aircraft Generation");
        aircraftTitle.setFont(Font.font(20));

        VBox inboundBox = new VBox(6);
        HBox inboundLine = new HBox(6, new Label("Arrival Rate:"), inboundValue, new Label("/hour"));
        inboundLine.setAlignment(Pos.CENTER_LEFT);
        inboundBox.getChildren().addAll(inboundLine, inboundSlider);

        VBox outboundBox = new VBox(6);
        HBox outboundLine = new HBox(6, new Label("Departure Rate:"), outboundValue, new Label("/hour"));
        outboundLine.setAlignment(Pos.CENTER_LEFT);
        outboundBox.getChildren().addAll(outboundLine, outboundSlider);

        aircraftPanel.getChildren().addAll(aircraftTitle, inboundBox, outboundBox);

        left.getChildren().addAll(simPanel, aircraftPanel);
        return left;
    }

    private VBox buildRightContent() {
        VBox right = new VBox(10);
        right.setStyle(PANEL);

        Label runwayTitle = new Label("Runway Configuration:");
        runwayTitle.setFont(Font.font(20));

        runwayGrid.setHgap(18);
        runwayGrid.setVgap(18);
        runwayGrid.setPadding(new Insets(8));

        // make columns expand evenly
        for (int c = 0; c < 5; c++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            cc.setFillWidth(true);
            cc.setPercentWidth(20);
            runwayGrid.getColumnConstraints().add(cc);
        }

        right.getChildren().addAll(runwayTitle, runwayGrid, errorLabel);
        VBox.setVgrow(runwayGrid, Priority.ALWAYS);

        errorLabel.setStyle("-fx-text-fill: #b00020;");
        return right;
    }

    private void initSliders() {
        runwaySlider.setMajorTickUnit(1);
        runwaySlider.setMinorTickCount(0);
        runwaySlider.setSnapToTicks(true);
        runwaySlider.setShowTickLabels(false);
        runwaySlider.setShowTickMarks(false);

        inboundSlider.setMajorTickUnit(5);
        inboundSlider.setShowTickLabels(false);
        inboundSlider.setShowTickMarks(false);

        outboundSlider.setMajorTickUnit(5);
        outboundSlider.setShowTickLabels(false);
        outboundSlider.setShowTickMarks(false);
    }

    // ---------------- runway cards ----------------

    private void buildRunwayCards() {
        runwayCards.clear();
        data.ensureRunwayListSize();

        for (int i = 1; i <= 10; i++) {
            // Ensure we have a runway config object for 1..runwayCount only,
            // but cards 1..10 always exist visually.
            SimulationData.RunwayConfig cfg = (i <= data.runways.size())
                    ? data.runways.get(i - 1)
                    : new SimulationData.RunwayConfig(i);

            runwayCards.add(new RunwayCard(cfg));
        }
    }

    private void refreshRunwayGrid() {
        data.ensureRunwayListSize();

        runwayGrid.getChildren().clear();

        // rebuild card config references for the active ones
        for (int i = 1; i <= 10; i++) {
            RunwayCard card = runwayCards.get(i - 1);

            // active configs come from data.runways
            if (i <= data.runwayCount) {
                card.bindTo(data.runways.get(i - 1));
                card.setEnabled(true);
            } else {
                // disabled cards still show but are greyed out
                card.setEnabled(false);
            }

            int idx = i - 1;
            int row = idx / 5;
            int col = idx % 5;

            runwayGrid.add(card.root, col, row);
        }
    }

    // ---------------- data/validation ----------------

    private void loadIntoControls(SimulationData d) {
        d.ensureRunwayListSize();

        runwaySlider.setValue(d.runwayCount);
        inboundSlider.setValue(d.inboundRatePerHour);
        outboundSlider.setValue(d.outboundRatePerHour);

        runwayValue.setText(String.valueOf(d.runwayCount));
        inboundValue.setText(String.valueOf(d.inboundRatePerHour));
        outboundValue.setText(String.valueOf(d.outboundRatePerHour));
    }

    private void onRun() {
        int runways = (int) Math.round(runwaySlider.getValue());
        int in = (int) Math.round(inboundSlider.getValue());
        int out = (int) Math.round(outboundSlider.getValue());

        if (runways < 1 || runways > 10) {
            errorLabel.setText("Runway count must be between 1 and 10.");
            return;
        }
        if (in < 0 || in > 30 || out < 0 || out > 30) {
            errorLabel.setText("Flow rates must be between 0 and 30 per hour.");
            return;
        }

        errorLabel.setText("");

        data.runwayCount = runways;
        data.inboundRatePerHour = in;
        data.outboundRatePerHour = out;
        data.ensureRunwayListSize();

        app.switchTo(AppState.RUNNING, data.copy());
    }

    // ---------------- runway card component (inner class, NOT extra file) ----------------

    private class RunwayCard {
        private final VBox root = new VBox(10);

        private final Label title = new Label();
        private final ComboBox<SimulationData.RunwayStatus> statusBox = new ComboBox<>();
        private final ComboBox<SimulationData.RunwayMode> modeBox = new ComboBox<>();

        private SimulationData.RunwayConfig bound;

        RunwayCard(SimulationData.RunwayConfig cfg) {
            root.setStyle(CARD);
            root.setMinWidth(180);

            title.setFont(Font.font(16));

            Label statusLabel = new Label("Operational Status");
            statusLabel.setStyle("-fx-font-weight: 700;");

            statusBox.getItems().setAll(SimulationData.RunwayStatus.values());

            Label modeLabel = new Label("Operating Mode");
            modeLabel.setStyle("-fx-font-weight: 700;");

            modeBox.getItems().setAll(SimulationData.RunwayMode.values());

            root.getChildren().addAll(title, statusLabel, statusBox, modeLabel, modeBox);

            bindTo(cfg);

            statusBox.valueProperty().addListener((obs, o, n) -> {
                if (bound != null && n != null) bound.status = n;
            });
            modeBox.valueProperty().addListener((obs, o, n) -> {
                if (bound != null && n != null) bound.mode = n;
            });
        }

        void bindTo(SimulationData.RunwayConfig cfg) {
            this.bound = cfg;
            title.setText("Runway " + cfg.id + ":");
            statusBox.setValue(cfg.status);
            modeBox.setValue(cfg.mode);
        }

        void setEnabled(boolean enabled) {
            root.setStyle(enabled ? CARD : CARD_DISABLED);
            statusBox.setDisable(!enabled);
            modeBox.setDisable(!enabled);
            root.setOpacity(enabled ? 1.0 : 0.85);
        }
    }
}