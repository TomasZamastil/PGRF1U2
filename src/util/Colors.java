package util;


import java.awt.*;

public class Colors {

    public static final int BACKGROUND = 0x000000;

    public static final int DEFAULT = 0x000000;

    public static final int PREVIEW_LINE = 0xff0000;

    public static final int MAIN_POLYGON = 0xffff00;

    public static final int CLIPPING_POLYGON = 0x0000ff;

    public static final int FILL = 0x009000;

    public static Color intToRGB(int rgbInt) {
            int r = (rgbInt >> 16) & 0xFF;
            int g = (rgbInt >> 8) & 0xFF;
            int b = rgbInt & 0xFF;
            return new Color(r, g, b);
    }

    public static int argbToRgb(int argb) {
        return argb & 0xffffff;
    }

    private Colors() {}

}
