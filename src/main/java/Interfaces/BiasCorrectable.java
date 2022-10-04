package Interfaces;

public class BiasCorrectable {
    protected boolean isBiasCorrected = false;

    public void biasCorrected() {
        this.isBiasCorrected = true;
    }

    public void nonBiasCorrected() {
        this.isBiasCorrected = false;
    }

    public boolean isBiasCorrected() {
        return this.isBiasCorrected;
    }
}
