package com.group32.cs261project.ui;

import java.io.IOException;

import com.group32.cs261project.app.dto.ScenarioConfig;
import com.group32.cs261project.app.SimulationHandle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainRouter {
	private final Stage primaryStage;
	private final UiState uiState;

	public MainRouter(Stage stage, UiState state) {
		this.primaryStage = stage;
		this.uiState = state;
	}

	public void showConfigure() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group32/cs261project/ui/configure/ConfigureView.fxml"));
		Parent root = loader.load();
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("Configure Simulation");
		primaryStage.show();
	}

	public void showRunning(SimulationHandle handle, ScenarioConfig cfg) throws IOException {
		uiState.setCurrentHandle(handle);
		uiState.setLastConfig(cfg);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group32/cs261project/ui/running/RunningView.fxml"));
		Parent root = loader.load();
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("Running Simulation");
		primaryStage.show();
	}

	public void showResults() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group32/cs261project/ui/results/ResultsView.fxml"));
		Parent root = loader.load();
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("Simulation Results");
		primaryStage.show();
	}
}
