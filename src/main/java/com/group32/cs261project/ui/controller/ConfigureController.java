package com.group32.cs261project.ui.controller;

import java.io.IOException;
import java.util.stream.IntStream;

import com.group32.cs261project.App;
import com.group32.cs261project.app.SimulationHandle;
import com.group32.cs261project.app.SimulationService;
import com.group32.cs261project.app.dto.RunwayConfig;
import com.group32.cs261project.app.dto.ScenarioConfig;
import com.group32.cs261project.ui.MainRouterHolder;
import com.group32.cs261project.ui.viewmodel.ConfigViewModel;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

public class ConfigureController {
	@FXML private Slider inboundSlider;
	@FXML private Slider outboundSlider;
	@FXML private Slider runwayCountSlider;
	@FXML private Label inboundLabel;
	@FXML private Label outboundLabel;
	@FXML private Label runwayCountLabel;
	@FXML private VBox runwayContainer;
	@FXML private Button runButton;

	private final ConfigViewModel vm = new ConfigViewModel();

	@FXML
	public void initialize() {
		inboundSlider.valueProperty().bindBidirectional(vm.inboundRateProperty());
		outboundSlider.valueProperty().bindBidirectional(vm.outboundRateProperty());
		runwayCountSlider.valueProperty().bindBidirectional(vm.runwayCountProperty());

		inboundLabel.textProperty().bind(vm.inboundRateProperty().asString());
		outboundLabel.textProperty().bind(vm.outboundRateProperty().asString());
		runwayCountLabel.textProperty().bind(vm.runwayCountProperty().asString());

		vm.runwayCountProperty().addListener((o,oldV,newV)-> rebuildRunwayCards(newV.intValue()));
		Platform.runLater(() -> rebuildRunwayCards(vm.runwayCountProperty().get()));

		runButton.setOnAction(evt -> onRun());
	}

	private void rebuildRunwayCards(int n) {
		runwayContainer.getChildren().clear();
		IntStream.range(0, n).forEach(i -> {
			Label lbl = new Label("Runway " + (i+1) + " (Available / Mixed)");
			runwayContainer.getChildren().add(lbl);
		});
	}

	private void onRun() {
		ScenarioConfig cfg = new ScenarioConfig();
		cfg.setInboundFlowRate(vm.inboundRateProperty().get());
		cfg.setOutboundFlowRate(vm.outboundRateProperty().get());
		int n = vm.runwayCountProperty().get();
		cfg.setRunwayCount(n);
		for (int i=0;i<n;i++) {
			cfg.getRunways().add(new RunwayConfig(i, RunwayConfig.OperationalStatus.AVAILABLE, RunwayConfig.OperatingMode.MIXED));
		}

		SimulationService svc = App.UI_STATE.getSimulationService();
		SimulationHandle handle = svc.start(cfg);
		App.UI_STATE.setLastConfig(cfg);
		try {
			MainRouterHolder.ROUTER.showRunning(handle, cfg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
