package com.group32.cs261project;

import com.group32.cs261project.SimulationData.RunwayConfig;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class RunningPage implements Page {

    private final App app;
    private final SimulationService sim;

    private final BorderPane root = new BorderPane();

    private final Label simTimeLabel = new Label("-");
    private final Label holdingLabel = new Label("-");
    private final Label takeoffLabel = new Label("-");

    private final TextArea runwayArea = new TextArea();

    private SimulationData currentConfig;

    public RunningPage(App app, SimulationService sim) {
        this.app = app;
        this.sim = sim;

        root.setPadding(new Insets(12));

        Label title = new Label("Running Simulation");
        title.setFont(Font.font(20));

        GridPane gp = new GridPane();
        gp.setHgap(12);
        gp.setVgap(10);
        gp.setPadding(new Insets(10));

        gp.add(new Label("Sim time (min):"), 0, 0);
        gp.add(simTimeLabel, 1, 0);

        gp.add(new Label("Holding queue:"), 0, 1);
        gp.add(holdingLabel, 1, 1);

        gp.add(new Label("Take-off queue:"), 0, 2);
        gp.add(takeoffLabel, 1, 2);

        runwayArea.setEditable(false);
        runwayArea.setPrefRowCount(10);

        Button endBtn = new Button("End Simulation");
        Button applyConfigBtn = new Button("Apply Current Config Again"); // optional

        endBtn.setOnAction(e -> onEnd());
        applyConfigBtn.setOnAction(e -> {
            if (currentConfig != null) sim.updateConfig(currentConfig);
        });

        HBox buttons = new HBox(10, endBtn, applyConfigBtn);
        buttons.setPadding(new Insets(10));

        VBox center = new VBox(10, gp, new Label("Runway Config (live):"), runwayArea, buttons);
        center.setPadding(new Insets(10));

        root.setTop(title);
        root.setCenter(center);
    }

    @Override
    public Parent getView() {
        return root;
    }

    @Override
    public void onEnter(Object data) {
        // data is SimulationData config from Configure page
        currentConfig = ((SimulationData) data).copy();
        currentConfig.ensureRunwayListSize();

        // reset UI
        simTimeLabel.setText("0");
        holdingLabel.setText("0");
        takeoffLabel.setText("0");
        runwayArea.setText(renderRunways(currentConfig));

        // start stub
        sim.start(currentConfig, snap -> Platform.runLater(() -> {
            simTimeLabel.setText(String.valueOf(snap.simMinute));
            holdingLabel.setText(String.valueOf(snap.holdingQueue));
            takeoffLabel.setText(String.valueOf(snap.takeoffQueue));
            runwayArea.setText(renderRunways(snap));
        }));
    }

    @Override
    public void onExit() {
        // nothing for now
    }

    private void onEnd() {
        SimulationData report = sim.stop();
        app.switchTo(AppState.RESULTS, report);
    }

    private String renderRunways(SimulationData d) {
        StringBuilder sb = new StringBuilder();
        d.ensureRunwayListSize();
        for (var r : d.runways) {
            sb.append("Runway ").append(r.id)
              .append(" | ").append(r.status)
              .append(" | ").append(r.mode)
              .append("\n");
        }
        return sb.toString();
    }
}