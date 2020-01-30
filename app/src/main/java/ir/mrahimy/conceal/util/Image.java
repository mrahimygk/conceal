package ir.mrahimy.conceal.util;

public class Image {

    static Rgb getRgb(int pixel) {
        int r = (pixel & 0xff0000) >> 16;
        int g = (pixel & 0x00ff00) >> 8;
        int b = (pixel & 0x0000ff) >> 0;

        return new Rgb(r, g, b);
    }
}
