package com.group32.cs261project.ui.controller;

import com.group32.cs261project.App;
import com.group32.cs261project.app.SimulationHandle;
import com.group32.cs261project.app.dto.SimulationReport;
import com.group32.cs261project.ui.MainRouterHolder;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ResultsController {
	@FXML private Label inputsLabel;
	@FXML private Label metricsLabel;
	@FXML private Button backButton;

	@FXML
	public void initialize() {
		SimulationHandle handle = App.UI_STATE.getCurrentHandle();
		SimulationReport report = handle == null ? null : handle.getReport();
		if (report != null) {
			inputsLabel.setText(String.format("Inbound=%d Outbound=%d Runways=%d",
				report.getInputs().getInboundFlowRate(), report.getInputs().getOutboundFlowRate(), report.getInputs().getRunwayCount()));

			metricsLabel.setText(String.format("MaxTO=%d AvgWait=%.1f MaxHolding=%d AvgHolding=%.1f",
				report.getMaxTakeoffQueue(), report.getAvgWaitingTimeMinutes(), report.getMaxHolding(), report.getAvgHoldingTimeMinutes()));
		} else {
			inputsLabel.setText("No report available");
			metricsLabel.setText("");
		}

		backButton.setOnAction(evt -> {
			try { MainRouterHolder.ROUTER.showConfigure(); } catch (Exception e) { e.printStackTrace(); }
		});
	}
}
