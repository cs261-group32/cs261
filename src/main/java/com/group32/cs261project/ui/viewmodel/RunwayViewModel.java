package com.group32.cs261project.ui.viewmodel;

import com.group32.cs261project.app.dto.RunwayConfig;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class RunwayViewModel {
    private final int id;
    private final ObjectProperty<RunwayConfig.OperationalStatus> operationalStatus = new SimpleObjectProperty<>();
    private final ObjectProperty<RunwayConfig.OperatingMode> operatingMode = new SimpleObjectProperty<>();

    public RunwayViewModel(int id) {
        this.id = id;
        this.operationalStatus.set(RunwayConfig.OperationalStatus.AVAILABLE);
        this.operatingMode.set(RunwayConfig.OperatingMode.MIXED);
    }

    public int getId() { return id; }
    public ObjectProperty<RunwayConfig.OperationalStatus> operationalStatusProperty() { return operationalStatus; }
    public ObjectProperty<RunwayConfig.OperatingMode> operatingModeProperty() { return operatingMode; }
}
