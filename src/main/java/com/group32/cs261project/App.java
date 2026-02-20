package com.group32.cs261project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.EnumMap;
import java.util.Map;

public class App extends Application {

    private Stage stage;
    private final SimulationService sim = new StubSimulationService();

    private final Map<AppState, Page> pages = new EnumMap<>(AppState.class);
    private AppState currentState = null;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;

        // create pages (one file per page)
        pages.put(AppState.CONFIGURE, new ConfigurePage(this, sim));
        pages.put(AppState.RUNNING, new RunningPage(this, sim));
        pages.put(AppState.RESULTS, new ResultsPage(this, sim));

        stage.setTitle("CS261 Airport Simulation (UI Stub)");
        stage.setScene(new Scene(pages.get(AppState.CONFIGURE).getView(), 900, 600));
        switchTo(AppState.CONFIGURE, null);
        stage.show();
    }

    public void switchTo(AppState next, Object data) {
        if (currentState != null) pages.get(currentState).onExit();
        currentState = next;

        Page page = pages.get(next);
        stage.getScene().setRoot(page.getView());
        page.onEnter(data);
    }

    public static void main(String[] args) {
        launch(args);
    }
}