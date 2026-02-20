package com.group32.cs261project;

import com.group32.cs261project.app.stub.StubSimulationService;
import com.group32.cs261project.ui.MainRouter;
import com.group32.cs261project.ui.MainRouterHolder;
import com.group32.cs261project.ui.UiState;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    public static UiState UI_STATE;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // initialize with stub service
        StubSimulationService stub = new StubSimulationService();
        UI_STATE = new UiState(stub);
        MainRouter router = new MainRouter(primaryStage, UI_STATE);
        UI_STATE = UI_STATE; // keep
        MainRouterHolder.ROUTER = router;
        router.showConfigure();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
