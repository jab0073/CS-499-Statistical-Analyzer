package Graphing;

public enum GraphTypes {
    HORIZONTAL_BAR ("Horizontal Bar Graph"),
    VERTICAL_BAR ("Vertical Bar Graph"),
    X_Y ("X-Y Graph"),
    NORMAL_CURVE ("Normal Curve"),
    PIE_CHART ("Pie Chart");

    private final String name;
    GraphTypes(String name){
        this.name = name;
    }

    public String getName(){ return this.name; }
}
