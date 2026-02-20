package com.group32.cs261project.ui.viewmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;

public class RunningViewModel {
    private final LongProperty simulatedMinutes = new SimpleLongProperty(0);
    private final IntegerProperty inboundQueue = new SimpleIntegerProperty(0);
    private final IntegerProperty outboundQueue = new SimpleIntegerProperty(0);
    private final IntegerProperty holdingQueue = new SimpleIntegerProperty(0);

    public LongProperty simulatedMinutesProperty() { return simulatedMinutes; }
    public IntegerProperty inboundQueueProperty() { return inboundQueue; }
    public IntegerProperty outboundQueueProperty() { return outboundQueue; }
    public IntegerProperty holdingQueueProperty() { return holdingQueue; }
}
