/**
 * @author Aled Jackson
 */
package com.myst.helper;

/**
 * Flattens a 2D array into a 1D array for openGL
 */
public class Flatten {

    /**
     * Flattens a 2D array into a 1D array for OpenGL
     * @param array 2D input array
     * @param outputArray 1D output array
     * @param <E> Type of the array
     * @return The 1D array returned of type E
     */
    public static < E > E[]  flatten(E[][] array, E[] outputArray){
        int xLength = array.length;
        int yLength = array[0].length;

        int i = 0;
        for(int y = 0; y < yLength; y ++){
            for(int x = 0; x < xLength; x++ ){
                outputArray[i] = array[x][y];
                i++;
            }
        }

        return outputArray;

    }

}
