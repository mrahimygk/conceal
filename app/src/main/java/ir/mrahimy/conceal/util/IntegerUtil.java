package ir.mrahimy.conceal.util;

public class IntegerUtil {
    static int removeLsBits(int in){
        return in & 248;
    }

    static int and(int in1, int in2){
        return in1 & in2;
    }
    static int or(int in1, int in2){
        return in1 | in2;
    }
}
