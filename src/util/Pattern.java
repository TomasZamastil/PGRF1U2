package util;

import static util.Colors.FILL;

public class Pattern {

    public static final int BACKGROUND = Colors.BACKGROUND + 1;

    private static final int[][] PATTERN = {
            {BACKGROUND, BACKGROUND, FILL, BACKGROUND, BACKGROUND},
            {BACKGROUND, BACKGROUND, FILL, BACKGROUND, BACKGROUND},
            {FILL, FILL, FILL, FILL, FILL},
            {BACKGROUND, BACKGROUND, FILL, BACKGROUND, BACKGROUND},
            {BACKGROUND, BACKGROUND, FILL, BACKGROUND, BACKGROUND}
    };

    private static final int LENGTH = PATTERN[0].length;   //always a square pattern

    public static int getPattern(int x, int y) {
        return PATTERN[x % LENGTH][y % LENGTH];
    }

    private Pattern() {
    }
}
