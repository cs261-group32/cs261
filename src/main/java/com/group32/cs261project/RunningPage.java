package com.group32.cs261project;

import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class RunningPage implements Page {

    private final App app;
    private final SimulationService sim;

    private final BorderPane root = new BorderPane();

    // Live metrics
    private final Label simTimeVal = new Label("0");
    private final Label holdingVal = new Label("0");
    private final Label takeoffVal = new Label("0");

    // Runway table
    private final GridPane runwayTable = new GridPane();
    private final List<Label> runwayModeVals = new ArrayList<>();
    private final List<Label> runwayStatusVals = new ArrayList<>();

    // Spinner animation (so we can stop it on exit)
    private PathTransition spinnerAnim;

    // Keep last config for restart
    private SimulationData currentConfig;

    // Styles (match your other pages)
    private static final String PAGE_BG = "-fx-background-color: white;";
    private static final String CARD =
            "-fx-background-color: white;" +
            "-fx-border-color: #111;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 18;";

    private static final String BTN_PRIMARY =
            "-fx-background-color: #111; -fx-text-fill: white; -fx-font-weight: 700; -fx-padding: 10 14;";

    private static final String BTN_SOFT =
            "-fx-background-color: #2f6dff; -fx-text-fill: white; -fx-font-weight: 700; -fx-padding: 10 14;";

    public RunningPage(App app, SimulationService sim) {
        this.app = app;
        this.sim = sim;

        root.setStyle(PAGE_BG);
        root.setPadding(new Insets(18));

        // ---- Title ----
        Label title = new Label("Running Simulation");
        title.setFont(Font.font(28));
        BorderPane.setMargin(title, new Insets(0, 0, 14, 0));
        root.setTop(title);

        // ---- Center content ----
        VBox center = new VBox(18);
        center.setPadding(new Insets(0));

        VBox metricsCard = buildMetricsCard();

        // Spinner (small, fixed size)
        Node spinner = buildPlaneSpinnerWithTrack(150, 50); // paneSize, radius

        VBox runwayCard = buildRunwayCard();
        VBox.setVgrow(runwayCard, Priority.ALWAYS);

        center.getChildren().addAll(metricsCard, spinner, runwayCard);
        VBox.setVgrow(runwayCard, Priority.ALWAYS);
        root.setCenter(center);

        // ---- Bottom buttons ----
        Button endBtn = new Button("End Simulation");
        endBtn.setStyle(BTN_PRIMARY);
        endBtn.setOnAction(e -> onEnd());

        Button restartBtn = new Button("Restart");
        restartBtn.setStyle(BTN_SOFT);
        restartBtn.setOnAction(e -> onRestart());

        HBox buttons = new HBox(14, endBtn, restartBtn);
        buttons.setAlignment(Pos.CENTER_LEFT);
        buttons.setPadding(new Insets(14, 0, 0, 0));
        root.setBottom(buttons);
    }

    @Override
    public Parent getView() {
        return root;
    }

    @Override
    public void onEnter(Object data) {
        currentConfig = ((SimulationData) data).copy();
        currentConfig.ensureRunwayListSize();

        // reset UI
        simTimeVal.setText("0");
        holdingVal.setText("0");
        takeoffVal.setText("0");

        rebuildRunwayTable(currentConfig.runwayCount);
        updateRunwayTable(currentConfig);

        // start animation
        if (spinnerAnim != null) {
            spinnerAnim.playFromStart();
        }

        // start simulation and update UI
        sim.start(currentConfig, snap -> Platform.runLater(() -> {
            simTimeVal.setText(String.valueOf(snap.simMinute));
            holdingVal.setText(String.valueOf(snap.holdingQueue));
            takeoffVal.setText(String.valueOf(snap.takeoffQueue));
            updateRunwayTable(snap);
        }));
    }

    @Override
    public void onExit() {
        // stop animation when leaving page
        if (spinnerAnim != null) {
            spinnerAnim.stop();
        }
    }

    // ---------------- UI BUILDERS ----------------

    private VBox buildMetricsCard() {
        VBox card = new VBox(12);
        card.setStyle(CARD);

        Label header = new Label("Live Status");
        header.setFont(Font.font(18));
        header.setStyle("-fx-font-weight: 800;");

        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);

        addRow(gp, 0, "Simulation time:", simTimeVal, "min");
        addRow(gp, 1, "Number of Aircraft in Holding Queue:", holdingVal, "");
        addRow(gp, 2, "Number of Aircraft in Take-off Queue:", takeoffVal, "");

        card.getChildren().addAll(header, gp);
        return card;
    }

    private VBox buildRunwayCard() {
        VBox card = new VBox(12);
        card.setStyle(CARD);

        Label header = new Label("Runway Configuration");
        header.setFont(Font.font(18));
        header.setStyle("-fx-font-weight: 800;");

        runwayTable.setHgap(12);
        runwayTable.setVgap(10);
        runwayTable.setPadding(new Insets(6, 0, 0, 0));

        ColumnConstraints c0 = new ColumnConstraints();
        c0.setMinWidth(110);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setHgrow(Priority.ALWAYS);

        ColumnConstraints c2 = new ColumnConstraints();
        c2.setHgrow(Priority.ALWAYS);

        runwayTable.getColumnConstraints().setAll(c0, c1, c2);

        ScrollPane tableScroll = new ScrollPane(runwayTable);
        tableScroll.setFitToWidth(true);
        tableScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // THIS is what makes the table area expand to fill the empty space
        tableScroll.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(tableScroll, Priority.ALWAYS);

        card.getChildren().addAll(header, new Separator(), tableScroll);

        // Let the whole card expand if its parent VBox allows it
        card.setMaxHeight(Double.MAX_VALUE);

        return card;
    }

    /**
     * Small plane loading spinner:
     * - fixed pane size
     * - light circle track
     * - plane (emoji) moves around circle using PathTransition
     */
    private Node buildPlaneSpinnerWithTrack(double paneSize, double radius) {
        StackPane wrap = new StackPane();
        wrap.setAlignment(Pos.CENTER);

        Pane pane = new Pane();
        pane.setPrefSize(paneSize, paneSize);
        pane.setMinSize(paneSize, paneSize);
        pane.setMaxSize(paneSize, paneSize);

        double cx = paneSize / 2.0;
        double cy = paneSize / 2.0;

        // track circle (visible)
        Circle track = new Circle(cx, cy, radius);
        track.setFill(null);
        track.setStroke(Color.LIGHTGRAY);
        track.setStrokeWidth(3);

        // path circle (can reuse the track as the path)
        Circle path = new Circle(cx, cy, radius);

        // plane symbol (simple + works without image assets)
        Label plane = new Label("✈");
        plane.setStyle("-fx-font-size: 22px;");
        // initial position doesn't matter; PathTransition will place it on the path

        // animate plane around the circle
        spinnerAnim = new PathTransition(Duration.seconds(1.8), path, plane);
        spinnerAnim.setCycleCount(Animation.INDEFINITE);
        spinnerAnim.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);

        // Add to pane
        pane.getChildren().addAll(track, plane);

        // Optional hint text (remove if you don't want it)
        Label hint = new Label("Simulation running…");
        hint.setStyle("-fx-text-fill: #444;");

        VBox box = new VBox(10, pane, hint);
        box.setAlignment(Pos.CENTER);

        wrap.getChildren().add(box);
        return wrap;
    }

    private void addRow(GridPane gp, int row, String labelText, Label valueLabel, String suffix) {
        Label l = new Label(labelText);

        valueLabel.setStyle("-fx-font-weight: 800;");
        valueLabel.setMaxWidth(Double.MAX_VALUE);
        valueLabel.setAlignment(Pos.CENTER_RIGHT);

        HBox valueBox = new HBox(6);
        valueBox.setAlignment(Pos.CENTER_RIGHT);
        valueBox.getChildren().add(valueLabel);

        if (suffix != null && !suffix.isBlank()) {
            Label s = new Label(suffix);
            s.setStyle("-fx-text-fill: #444;");
            valueBox.getChildren().add(s);
        }

        gp.add(l, 0, row);
        gp.add(valueBox, 1, row);

        ColumnConstraints c0 = new ColumnConstraints();
        c0.setHgrow(Priority.ALWAYS);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setMinWidth(220);
        c1.setHgrow(Priority.NEVER);

        gp.getColumnConstraints().setAll(c0, c1);
    }

    // ---------------- RUNWAY TABLE ----------------

    private void rebuildRunwayTable(int runwayCount) {
        runwayTable.getChildren().clear();
        runwayModeVals.clear();
        runwayStatusVals.clear();

        Label h0 = new Label("Runway");
        Label h1 = new Label("Operating Mode");
        Label h2 = new Label("Operational Status");
        h0.setStyle("-fx-font-weight: 800;");
        h1.setStyle("-fx-font-weight: 800;");
        h2.setStyle("-fx-font-weight: 800;");

        runwayTable.add(h0, 0, 0);
        runwayTable.add(h1, 1, 0);
        runwayTable.add(h2, 2, 0);

        for (int i = 0; i < runwayCount; i++) {
            int row = i + 1;

            Label name = new Label("Runway " + (i + 1) + ":");
            Label mode = new Label("-");
            Label status = new Label("-");

            mode.setStyle("-fx-font-weight: 700;");
            status.setStyle("-fx-font-weight: 700;");

            runwayModeVals.add(mode);
            runwayStatusVals.add(status);

            runwayTable.add(name, 0, row);
            runwayTable.add(mode, 1, row);
            runwayTable.add(status, 2, row);
        }
    }

    private void updateRunwayTable(SimulationData d) {
        d.ensureRunwayListSize();

        int n = Math.min(d.runways.size(), runwayModeVals.size());
        for (int i = 0; i < n; i++) {
            var r = d.runways.get(i);
            runwayModeVals.get(i).setText(pretty(r.mode.name()));
            runwayStatusVals.get(i).setText(pretty(r.status.name()));
        }
    }

    private String pretty(String enumName) {
        String lower = enumName.toLowerCase().replace('_', ' ');
        String[] parts = lower.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (p.isBlank()) continue;
            sb.append(Character.toUpperCase(p.charAt(0))).append(p.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    // ---------------- ACTIONS ----------------

    private void onEnd() {
        SimulationData report = sim.stop();
        app.switchTo(AppState.RESULTS, report);
    }

    private void onRestart() {
        sim.stop();
        app.switchTo(AppState.CONFIGURE, null);
    }
}