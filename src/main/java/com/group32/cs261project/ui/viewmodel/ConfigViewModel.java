package com.group32.cs261project.ui.viewmodel;

import com.group32.cs261project.app.dto.RunwayConfig;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ConfigViewModel {
    private final IntegerProperty inboundRate = new SimpleIntegerProperty(5);
    private final IntegerProperty outboundRate = new SimpleIntegerProperty(5);
    private final IntegerProperty runwayCount = new SimpleIntegerProperty(1);
    private final ObservableList<RunwayConfig> runways = FXCollections.observableArrayList();

    public ConfigViewModel() {
        rebuildRunways();
        runwayCount.addListener((obs,oldV,newV)-> rebuildRunways());
    }

    private void rebuildRunways() {
        int n = Math.max(1, runwayCount.get());
        runways.clear();
        for (int i=0;i<n;i++) {
            runways.add(new RunwayConfig(i, RunwayConfig.OperationalStatus.AVAILABLE, RunwayConfig.OperatingMode.MIXED));
        }
    }

    public IntegerProperty inboundRateProperty() { return inboundRate; }
    public IntegerProperty outboundRateProperty() { return outboundRate; }
    public IntegerProperty runwayCountProperty() { return runwayCount; }
    public ObservableList<RunwayConfig> getRunways() { return runways; }
}
