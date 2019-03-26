/**
 * @author Aled Jackson
 */
package com.myst.helper;

/**
 * Gets the time in seconds
 */
public class Timer {
    public static double getTime(){
//        converts to seconds
        return (double) System.nanoTime() / 1000000000L;
    }
}
