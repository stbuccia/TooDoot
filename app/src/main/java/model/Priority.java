package model;

import java.io.Serializable;

public class Priority implements Serializable {
    private int value;
    public int maxVal = 26;
    public int minVal = 0; //0 is unknown
    public int defaultVal = 0;

    public Priority(int v){
        if (v <= maxVal && v >= minVal)
            this.value = v;
        else
            this.value = defaultVal;
    }

    public Priority(char c){
        new Priority(fromCharToInt(c));
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
            return (char) ((v - 1) + 'A');
    }

    public static int fromCharToInt(char c){
        if (c == '0')
            return 0;
        else
            return (int)(c - 'A') + 1;
    }

    public boolean isNull(){
        return value == defaultVal;
    }

    public char getCharValue(){
        return fromIntToChar(value);
    }

}