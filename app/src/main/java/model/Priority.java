package model;

public class Priority {
    private int value;
    public int maxVal = 5;
    public int minVal = 1;
    public int defaultVal = 3;

    Priority(int v){
        if (v <= maxVal && v >= minVal)
            value = v;
        else
            value = defaultVal;
    }

    public void setValue(int v) {
        if (v <= maxVal && v >= minVal)
            value = v;
    }

    public int getValue() {
        return value;
    }

}