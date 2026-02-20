package com.group32.cs261project.ui.viewmodel;

import com.group32.cs261project.app.dto.SimulationReport;

public class ResultsViewModel {
    private SimulationReport report;

    public SimulationReport getReport() { return report; }
    public void setReport(SimulationReport report) { this.report = report; }
}
