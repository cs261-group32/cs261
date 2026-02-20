package com.group32.cs261project.ui.controller;

import java.io.IOException;

import com.group32.cs261project.App;
import com.group32.cs261project.app.SimulationHandle;
import com.group32.cs261project.app.dto.SimulationSnapshot;
import com.group32.cs261project.ui.MainRouterHolder;
import com.group32.cs261project.ui.viewmodel.RunningViewModel;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class RunningController {
	@FXML private Label timeLabel;
	@FXML private Label inboundLabel;
	@FXML private Label outboundLabel;
	@FXML private Label holdingLabel;
	@FXML private Button endButton;

	private final RunningViewModel vm = new RunningViewModel();
	private SimulationHandle handle;

	@FXML
	public void initialize() {
		// bind labels
		timeLabel.textProperty().bind(vm.simulatedMinutesProperty().asString("%d min"));
		inboundLabel.textProperty().bind(vm.inboundQueueProperty().asString());
		outboundLabel.textProperty().bind(vm.outboundQueueProperty().asString());
		holdingLabel.textProperty().bind(vm.holdingQueueProperty().asString());

		// get current handle
		handle = App.UI_STATE.getCurrentHandle();
		if (handle != null) {
			handle.onSnapshot(this::onSnapshot);
		}

		endButton.setOnAction(evt -> {
			if (handle != null) handle.stop();
			try { MainRouterHolder.ROUTER.showResults(); } catch (IOException e) { e.printStackTrace(); }
		});
	}

	private void onSnapshot(SimulationSnapshot snap) {
		Platform.runLater(() -> {
			vm.simulatedMinutesProperty().set(snap.getSimulatedMinutes());
			vm.inboundQueueProperty().set(snap.getInboundQueue());
			vm.outboundQueueProperty().set(snap.getOutboundQueue());
			vm.holdingQueueProperty().set(snap.getHoldingQueue());
		});
	}
}
