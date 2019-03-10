package com.myst.world.collisions;

import org.joml.Matrix3f;
import org.joml.Vector2f;

public class Line {
    public Vector2f vector;
    public Vector2f position;

    public Line( Vector2f position, Vector2f vector){
        this.vector = vector;
        this.position = position;
    }

//    returns the intersection vector
    public Vector2f intersection(Line line2){
        float[][] gaussMatrix = new float[3][2];
        gaussMatrix[2][0] = this.position.x - line2.position.x;
        gaussMatrix[2][1] = this.position.y - line2.position.y;
        gaussMatrix[0][0] = -this.vector.x;
        gaussMatrix[0][1] = -this.vector.y;
        gaussMatrix[1][0] = line2.vector.x;
        gaussMatrix[1][1] = line2.vector.y;


        float n = -gaussMatrix[0][1] / gaussMatrix[0][0];
        gaussMatrix[0][1] = 0;
        gaussMatrix[1][1] = gaussMatrix[1][1] + (n * gaussMatrix[1][0]);
        gaussMatrix[2][1] = gaussMatrix[2][1] + (n * gaussMatrix[2][0]);


        float lambda = gaussMatrix[2][1];

        if(lambda < 0){
            return null;
        }

        Vector2f intersection = new Vector2f();
        intersection.x = (lambda * this.vector.x) + this.position.x;
        intersection.y = (lambda * this.vector.y) + this.position.y;

        return intersection;
    }

//    returns the coefficient of intersection for the second vector
    public Float intersectionCo(Line line2){
        if(this.vector.x == line2.vector.x && this.vector.y == line2.vector.y){
            return null;
        }

        float[][] gaussMatrix = new float[3][2];
        gaussMatrix[2][0] = line2.position.x - this.position.x;
        gaussMatrix[2][1] = line2.position.y - this.position.y;
        gaussMatrix[0][0] = this.vector.x;
        gaussMatrix[0][1] = this.vector.y;
        gaussMatrix[1][0] = -line2.vector.x;
        gaussMatrix[1][1] = -line2.vector.y;


//        eliminating 0 1
        float n = -gaussMatrix[0][1] / gaussMatrix[0][0];
        gaussMatrix[0][1] = 0;
        gaussMatrix[1][1] = gaussMatrix[1][1] + (n * gaussMatrix[1][0]);
        gaussMatrix[2][1] = gaussMatrix[2][1] + (n * gaussMatrix[2][0]);

//        eliminating 1 0
        n = -gaussMatrix[1][0] / gaussMatrix[1][1];
        gaussMatrix[1][0] = 0;
        gaussMatrix[2][0] = gaussMatrix[2][0] + (n * gaussMatrix[2][1]);

//        calculating the coefficients
        float lTwoLambda = gaussMatrix[2][1] / gaussMatrix[1][1];
        float lOneLambda = gaussMatrix[2][0] / gaussMatrix[0][0];

        if(lOneLambda < 0){
            return null;
        }

        return lTwoLambda;
    }
}