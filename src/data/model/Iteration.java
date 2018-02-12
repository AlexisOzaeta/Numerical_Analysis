package data.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Iteration {
    public SimpleIntegerProperty iteration = new SimpleIntegerProperty();
    public SimpleDoubleProperty xa = new SimpleDoubleProperty();
    public SimpleDoubleProperty xb = new SimpleDoubleProperty();
    public SimpleDoubleProperty fxa = new SimpleDoubleProperty();
    public SimpleDoubleProperty fxb = new SimpleDoubleProperty();
    public SimpleDoubleProperty xr = new SimpleDoubleProperty();
    public SimpleDoubleProperty error = new SimpleDoubleProperty();

    public int getIteration() {
        return iteration.get();
    }

    public double getXa() {
        return xa.get();
    }

    public double getXb() {
        return xb.get();
    }

    public double getFxa() {
        return fxa.get();
    }

    public double getFxb() {
        return fxb.get();
    }

    public double getXr() {
        return xr.get();
    }

    public double getError() {
        return error.get();
    }
    
    
}
