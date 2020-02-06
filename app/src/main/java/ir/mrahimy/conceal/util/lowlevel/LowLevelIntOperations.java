package ir.mrahimy.conceal.util.lowlevel;

public class LowLevelIntOperations {
    public static int removeLsBits(int in) {
        return in & 248;
    }

    public static int get2LsBits(int in) {
        return in & 3;
    }

    public static int get3LsBits(int in) {
        return in & 7;
    }

    public static int and(int in1, int in2) {
        return in1 & in2;
    }

    public static int or(int in1, int in2) {
        return in1 | in2;
    }
}
