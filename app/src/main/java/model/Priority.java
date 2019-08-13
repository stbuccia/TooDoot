package model;

public class Priority {
    private int value;
    public int maxVal = 26;
    public int minVal = 0; //0 is unknown
    public int defaultVal = 0;

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

    private char fromIntToChar(int v){
        if (v == 0)
            return '0';
        else
            return (char) (v + 64);
    }

    public static int fromCharToInt(char c){
        if (c == '0')
            return 0;
        else
            return (int)(c - 64);
    }

    public char getCharValue(){
        return fromIntToChar(value);
    }

}