package com.group32.cs261project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class ResultsPage implements Page {

    private final App app;
    private final SimulationService sim;

    private final BorderPane root = new BorderPane();

    // top title
    private final Label title = new Label("End-of-Run Summary");

    // left card dynamic labels
    private final Label inboundRateVal = new Label("-");
    private final Label outboundRateVal = new Label("-");
    private final Label runwayCountVal = new Label("-");
    private final Label maxWaitVal = new Label("-");

    private final Label runwayModesVal = new Label("-");
    private final Label runwayStatusVal = new Label("-");

    // right card dynamic labels
    private final Label maxTakeoffQueueVal = new Label("-");
    private final Label avgTakeoffWaitVal = new Label("-");

    private final Label maxHoldingVal = new Label("-");
    private final Label avgHoldingVal = new Label("-");

    private final Label maxInboundDelayVal = new Label("-");
    private final Label avgInboundDelayVal = new Label("-");

    private final Label cancelledVal = new Label("-");
    private final Label divertedVal = new Label("-");

    // styles (simple, like your screenshot)
    private static final String PAGE_BG = "-fx-background-color: white;";
    private static final String CARD =
            "-fx-background-color: white;" +
            "-fx-border-color: #111;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 18;";

    public ResultsPage(App app, SimulationService sim) {
        this.app = app;
        this.sim = sim;

        root.setStyle(PAGE_BG);
        root.setPadding(new Insets(18));

        // ---------- Top ----------
        title.setFont(Font.font(28));
        BorderPane.setMargin(title, new Insets(0, 0, 14, 0));
        root.setTop(title);

        // ---------- Main two-column layout ----------
        HBox main = new HBox(22);

        VBox left = buildGeneralInfoCard();
        VBox right = buildRightCards();

        HBox.setHgrow(left, Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);

        left.setPrefWidth(520);
        right.setPrefWidth(420);

        main.getChildren().addAll(left, right);
        root.setCenter(main);

        // optional back button at bottom (not in screenshot, but useful)
        Button back = new Button("Back to Configure");
        back.setOnAction(e -> app.switchTo(AppState.CONFIGURE, null));
        back.setStyle("-fx-background-color: #111; -fx-text-fill: white; -fx-font-weight: 700; -fx-padding: 10 14;");
        BorderPane.setMargin(back, new Insets(14, 0, 0, 0));
        root.setBottom(back);
    }

    @Override
    public Parent getView() {
        return root;
    }

    @Override
    public void onEnter(Object data) {
        SimulationData d = (SimulationData) data;
        fillFrom(d);
    }

    // ---------------- UI builders ----------------

    private VBox buildGeneralInfoCard() {
        VBox card = new VBox(14);
        card.setStyle(CARD);

        Label header = new Label("General Information");
        header.setFont(Font.font(18));
        header.setStyle("-fx-font-weight: 800;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        addRow(grid, 0, "Inbound flow rate:", inboundRateVal);
        addRow(grid, 1, "Outbound flow rate:", outboundRateVal);
        addRow(grid, 2, "Number of runways:", runwayCountVal);
        addRow(grid, 3, "Maximum wait time:", maxWaitVal);

        Separator sep = new Separator();
        sep.setPadding(new Insets(8, 0, 8, 0));

        VBox runwayInfo = new VBox(10);

        Label modesHeader = new Label("Runway operating modes:");
        modesHeader.setStyle("-fx-font-weight: 800;");

        runwayModesVal.setWrapText(true);

        Label statusHeader = new Label("Runway operational statuses:");
        statusHeader.setStyle("-fx-font-weight: 800;");

        runwayStatusVal.setWrapText(true);

        runwayInfo.getChildren().addAll(
                modesHeader, runwayModesVal,
                statusHeader, runwayStatusVal
        );

        VBox.setVgrow(runwayInfo, Priority.ALWAYS);

        card.getChildren().addAll(header, grid, sep, runwayInfo);
        VBox.setVgrow(runwayInfo, Priority.ALWAYS);

        return card;
    }

    private VBox buildRightCards() {
        VBox right = new VBox(16);

        right.getChildren().addAll(
                smallCard("Take-off Queue",
                        "Maximum number of planes in take-off queue:", maxTakeoffQueueVal,
                        "Average wait time in take-off queue:", avgTakeoffWaitVal
                ),
                smallCard("Holding Pattern",
                        "Maximum number of planes in holding pattern:", maxHoldingVal,
                        "Average hold time for inbound aircraft:", avgHoldingVal
                ),
                smallCard("Delays",
                        "Maximum inbound delay:", maxInboundDelayVal,
                        "Average inbound delay:", avgInboundDelayVal
                ),
                smallCard("Cancellations and Diversions",
                        "Total planes cancelled:", cancelledVal,
                        "Inbound aircraft diverted due to low fuel:", divertedVal
                )
        );

        return right;
    }

    private VBox smallCard(String titleText,
                           String label1, Label value1,
                           String label2, Label value2) {

        VBox card = new VBox(12);
        card.setStyle(CARD);

        Label header = new Label(titleText);
        header.setFont(Font.font(18));
        header.setStyle("-fx-font-weight: 800;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        addRow(grid, 0, label1, value1);
        addRow(grid, 1, label2, value2);

        card.getChildren().addAll(header, grid);
        return card;
    }

    private void addRow(GridPane grid, int row, String labelText, Label valueLabel) {
        Label l = new Label(labelText);

        valueLabel.setStyle("-fx-font-weight: 800;");
        valueLabel.setAlignment(Pos.CENTER_RIGHT);
        valueLabel.setMaxWidth(Double.MAX_VALUE);

        grid.add(l, 0, row);
        grid.add(valueLabel, 1, row);

        ColumnConstraints c0 = new ColumnConstraints();
        c0.setHgrow(Priority.ALWAYS);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setMinWidth(140);
        c1.setHgrow(Priority.NEVER);

        grid.getColumnConstraints().setAll(c0, c1);
    }

    // ---------------- Populate values ----------------

    private void fillFrom(SimulationData d) {
        d.ensureRunwayListSize();

        // General info
        inboundRateVal.setText(String.format("%.2f aircraft/hour", (double) d.inboundRatePerHour));
        outboundRateVal.setText(String.format("%.2f aircraft/hour", (double) d.outboundRatePerHour));
        runwayCountVal.setText(String.valueOf(d.runwayCount));
        maxWaitVal.setText(String.format("%.2f minutes", (double) d.maxWaitMinutes));

        // runway lists
        StringBuilder modes = new StringBuilder();
        StringBuilder status = new StringBuilder();
        for (int i = 0; i < d.runways.size(); i++) {
            var r = d.runways.get(i);
            modes.append("Runway ").append(r.id).append(": ").append(pretty(r.mode.name())).append("\n");
            status.append("Runway ").append(r.id).append(": ").append(pretty(r.status.name())).append("\n");
        }
        runwayModesVal.setText(modes.toString().trim());
        runwayStatusVal.setText(status.toString().trim());

        // Take-off Queue (we don’t currently track “avg wait time” separately,
        // so we map it to avgTakeoffQueue as a proxy in minutes like your stub does)
        maxTakeoffQueueVal.setText(String.valueOf(d.maxTakeoffQueue));
        avgTakeoffWaitVal.setText(String.format("%.2f minutes", d.avgTakeoffQueue));

        // Holding Pattern
        maxHoldingVal.setText(String.valueOf(d.maxHoldingQueue));
        avgHoldingVal.setText(String.format("%.2f minutes", d.avgHoldingQueue));

        // Delays
        maxInboundDelayVal.setText(String.format("%.2f minutes", (double) d.maxInboundDelayMin));
        avgInboundDelayVal.setText(String.format("%.2f minutes", d.avgInboundDelayMin));

        // Cancellations/Diversions
        cancelledVal.setText(String.valueOf(d.cancelledCount));
        divertedVal.setText(String.valueOf(d.divertedCount));
    }

    private String pretty(String enumName) {
        // RUNWAY_INSPECTION -> Runway Inspection
        String lower = enumName.toLowerCase().replace('_', ' ');
        String[] parts = lower.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (p.isBlank()) continue;
            sb.append(Character.toUpperCase(p.charAt(0))).append(p.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}