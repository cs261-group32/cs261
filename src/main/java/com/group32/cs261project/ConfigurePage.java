package com.group32.cs261project;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class ConfigurePage implements Page {

    private final App app;
    private final SimulationService sim;

    private final BorderPane root = new BorderPane();

    private final Slider runwaySlider = new Slider(1, 10, 2);
    private final Slider inboundSlider = new Slider(0, 30, 10);
    private final Slider outboundSlider = new Slider(0, 30, 10);

    private final Label runwayValue = new Label();
    private final Label inboundValue = new Label();
    private final Label outboundValue = new Label();

    private final VBox runwayBox = new VBox(8);
    private final Label errorLabel = new Label();

    private SimulationData data = new SimulationData();

    public ConfigurePage(App app, SimulationService sim) {
        this.app = app;
        this.sim = sim;

        root.setPadding(new Insets(12));

        Label title = new Label("System Settings");
        title.setFont(Font.font(20));

        // sliders
        runwaySlider.setMajorTickUnit(1);
        runwaySlider.setMinorTickCount(0);
        runwaySlider.setSnapToTicks(true);
        runwaySlider.setShowTickLabels(true);

        inboundSlider.setMajorTickUnit(5);
        inboundSlider.setShowTickLabels(true);

        outboundSlider.setMajorTickUnit(5);
        outboundSlider.setShowTickLabels(true);

        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setPadding(new Insets(10));

        int r = 0;
        gp.add(new Label("Runways (1–10)"), 0, r);
        gp.add(runwaySlider, 1, r);
        gp.add(runwayValue, 2, r++);

        gp.add(new Label("Inbound flow (0–30 / hr)"), 0, r);
        gp.add(inboundSlider, 1, r);
        gp.add(inboundValue, 2, r++);

        gp.add(new Label("Outbound flow (0–30 / hr)"), 0, r);
        gp.add(outboundSlider, 1, r);
        gp.add(outboundValue, 2, r++);

        runwayBox.setPadding(new Insets(10));
        ScrollPane runwayScroll = new ScrollPane(runwayBox);
        runwayScroll.setFitToWidth(true);
        runwayScroll.setPrefHeight(320);

        errorLabel.setStyle("-fx-text-fill: #b00020;");

        Button runBtn = new Button("Run Simulation");
        Button resetBtn = new Button("Reset");

        runBtn.setOnAction(e -> onRun());
        resetBtn.setOnAction(e -> {
            data = new SimulationData();
            loadIntoControls(data);
            rebuildRunwayCards();
            errorLabel.setText("");
        });

        HBox buttons = new HBox(10, runBtn, resetBtn);
        buttons.setPadding(new Insets(10));

        VBox center = new VBox(10, gp, new Label("Runway Configuration"), runwayScroll, errorLabel, buttons);
        center.setPadding(new Insets(10));

        root.setTop(title);
        BorderPane.setMargin(title, new Insets(0,0,10,0));
        root.setCenter(center);

        // live updates
        runwaySlider.valueProperty().addListener((obs, o, n) -> {
            runwayValue.setText(String.valueOf((int) Math.round(n.doubleValue())));
            rebuildRunwayCards();
        });
        inboundSlider.valueProperty().addListener((obs, o, n) -> inboundValue.setText(String.valueOf((int) Math.round(n.doubleValue()))));
        outboundSlider.valueProperty().addListener((obs, o, n) -> outboundValue.setText(String.valueOf((int) Math.round(n.doubleValue()))));

        // init
        loadIntoControls(data);
        rebuildRunwayCards();
    }

    @Override
    public Parent getView() {
        return root;
    }

    @Override
    public void onEnter(Object ignored) {
        // do nothing; keep last state
    }

    private void loadIntoControls(SimulationData d) {
        runwaySlider.setValue(d.runwayCount);
        inboundSlider.setValue(d.inboundRatePerHour);
        outboundSlider.setValue(d.outboundRatePerHour);
        runwayValue.setText(String.valueOf(d.runwayCount));
        inboundValue.setText(String.valueOf(d.inboundRatePerHour));
        outboundValue.setText(String.valueOf(d.outboundRatePerHour));
    }

    private void rebuildRunwayCards() {
        // update config object from sliders
        data.runwayCount = (int) Math.round(runwaySlider.getValue());
        data.inboundRatePerHour = (int) Math.round(inboundSlider.getValue());
        data.outboundRatePerHour = (int) Math.round(outboundSlider.getValue());
        data.ensureRunwayListSize();

        runwayBox.getChildren().clear();

        for (int i = 0; i < data.runways.size(); i++) {
            SimulationData.RunwayConfig rw = data.runways.get(i);

            Label name = new Label("Runway " + rw.id);

            ComboBox<SimulationData.RunwayStatus> status = new ComboBox<>();
            status.getItems().setAll(SimulationData.RunwayStatus.values());
            status.setValue(rw.status);
            status.valueProperty().addListener((obs, o, n) -> rw.status = n);

            ComboBox<SimulationData.RunwayMode> mode = new ComboBox<>();
            mode.getItems().setAll(SimulationData.RunwayMode.values());
            mode.setValue(rw.mode);
            mode.valueProperty().addListener((obs, o, n) -> rw.mode = n);

            GridPane row = new GridPane();
            row.setHgap(10);
            row.setVgap(6);
            row.setPadding(new Insets(8));
            row.setStyle("-fx-border-color: #ddd; -fx-border-radius: 8; -fx-background-radius: 8;");

            row.add(name, 0, 0);
            row.add(new Label("Status:"), 0, 1);
            row.add(status, 1, 1);
            row.add(new Label("Mode:"), 0, 2);
            row.add(mode, 1, 2);

            ColumnConstraints c0 = new ColumnConstraints();
            c0.setMinWidth(80);
            ColumnConstraints c1 = new ColumnConstraints();
            c1.setHgrow(Priority.ALWAYS);
            row.getColumnConstraints().setAll(c0, c1);

            runwayBox.getChildren().add(row);
        }
    }

    private void onRun() {
        // validate hard
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
}