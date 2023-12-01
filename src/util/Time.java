package util;

public class Time {


    public static double timeStarted = System.nanoTime();
    public static long tick = 0;

    public static double getTime(){return (System.nanoTime() - timeStarted) * 1E-9;}
}