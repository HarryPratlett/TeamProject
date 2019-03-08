package com.myst.helper;

public class Flatten {

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
