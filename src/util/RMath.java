package util;

public class RMath {
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

}
