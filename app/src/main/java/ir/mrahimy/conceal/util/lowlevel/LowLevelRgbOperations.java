package ir.mrahimy.conceal.util.lowlevel;

import ir.mrahimy.conceal.data.Rgb;

public class LowLevelRgbOperations {

    /**
     * A pixel from a Bitmap.getPixel(x,y) is an integer which contains all RGB values inside.
     * This is a method to get the rgb values from this integer
     *
     * @param pixel a signed integer to get rgb values
     * @return a data class with separated RGB values
     */
    public static Rgb getRgb(int pixel) {
        int r = (pixel & 0xff0000) >> 16;
        int g = (pixel & 0x00ff00) >> 8;
        int b = (pixel & 0x0000ff) >> 0;

        return new Rgb(r, g, b);
    }

    /**
     * puts RGB values inside a signed integer
     *
     * @param in the separated RGB values to be put inside the integer.
     * @return a signed integer
     */
    public static int parseRgb(Rgb in) {
        int rgb = in.getR();
        rgb = (rgb << 8) + in.getG();
        rgb = (rgb << 8) + in.getB();
        return rgb;
    }
}
