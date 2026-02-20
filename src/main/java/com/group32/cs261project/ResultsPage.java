package com.group32.cs261project;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class ResultsPage implements Page {

    private final App app;
    private final SimulationService sim;

    private final BorderPane root = new BorderPane();
    private final Label summary = new Label();

    public ResultsPage(App app, SimulationService sim) {
        this.app = app;
        this.sim = sim;

        root.setPadding(new Insets(12));

        Label title = new Label("Results");
        title.setFont(Font.font(20));

        summary.setWrapText(true);

        Button backBtn = new Button("Back to Configure");
        backBtn.setOnAction(e -> app.switchTo(AppState.CONFIGURE, null));

        VBox center = new VBox(10, summary, backBtn);
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
        SimulationData d = (SimulationData) data;
        summary.setText(render(d));
    }

    private String render(SimulationData d) {
        StringBuilder sb = new StringBuilder();

        sb.append("Configuration:\n");
        sb.append("Runways: ").append(d.runwayCount).append("\n");
        sb.append("Inbound rate: ").append(d.inboundRatePerHour).append(" / hr\n");
        sb.append("Outbound rate: ").append(d.outboundRatePerHour).append(" / hr\n");
        sb.append("Max wait time: ").append(d.maxWaitMinutes).append(" min\n\n");

        sb.append("Metrics:\n");
        sb.append("Max holding queue: ").append(d.maxHoldingQueue).append("\n");
        sb.append("Avg holding queue: ").append(String.format("%.2f", d.avgHoldingQueue)).append("\n");
        sb.append("Max takeoff queue: ").append(d.maxTakeoffQueue).append("\n");
        sb.append("Avg takeoff queue: ").append(String.format("%.2f", d.avgTakeoffQueue)).append("\n\n");

        sb.append("Delays (proxy):\n");
        sb.append("Avg inbound delay (min): ").append(String.format("%.2f", d.avgInboundDelayMin)).append("\n");
        sb.append("Max inbound delay (min): ").append(d.maxInboundDelayMin).append("\n");
        sb.append("Avg outbound delay (min): ").append(String.format("%.2f", d.avgOutboundDelayMin)).append("\n");
        sb.append("Max outbound delay (min): ").append(d.maxOutboundDelayMin).append("\n\n");

        sb.append("Cancelled: ").append(d.cancelledCount).append("\n");
        sb.append("Diverted: ").append(d.divertedCount).append("\n");

        return sb.toString();
    }
}