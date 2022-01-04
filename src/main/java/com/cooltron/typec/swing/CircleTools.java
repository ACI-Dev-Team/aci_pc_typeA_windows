package com.cooltron.typec.swing;

import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;

public class CircleTools {
    private static final int[] x = { 32,   6,  1, 23, 53, 100, 149, 177, 199, 194, 169};
    private static final int[] y = {176, 137, 93, 40, 16,   4,  17,  40,  93, 138, 176};
    private static final int[] length =     {  0,  28,  55,  83, 110, 138, 165, 193, 220, 248, 275};
    private static final int[] startAngle = {228, 200, 173, 145, 118,  90,  63,  35,   8, -20, -47};
    private static final int relativeCenterX = 470;
    private static final int relativeCenterY = 250;
    public static int calFeedByPosition(double sceneX, double sceneY){
        double angle = getAngle( 0, 0, sceneX-relativeCenterX, sceneY-relativeCenterY);
        if(angle>=270 && angle<327){
            return 0;
        }else if(angle>=327 && angle<351){
            return 1;
        }else if(angle>=351 && angle<=360 || angle<20){
            return 2;
        }else if(angle>=20 && angle<49){
            return 3;
        }else if(angle>=49 && angle<74){
            return 4;
        }else if(angle>=74 && angle<105){
            return 5;
        }else if(angle>=105 && angle<131){
            return 6;
        }else if(angle>=131 && angle<158){
            return 7;
        }else if(angle>=158 && angle<188){
            return 8;
        }else if(angle>=188 && angle<212){
            return 9;
        }else{
            return 10;
        }

    }

    public static double getAngle(double x1,double  y1,double  x2,double  y2) {
        double x = x1 - x2,y = y1 - y2;
        double angle = (180 + Math.atan2(-y, -x) * 180 / Math.PI + 360) % 360;
        return angle;
    }

    public static void showCircle(int speak, Circle circle, Arc circleProgress){
        if(speak<0 || speak>10){
            return;
        }
        circle.setLayoutX(x[speak]);
        circle.setLayoutY(y[speak]);
        circleProgress.setStartAngle(startAngle[speak]);
        circleProgress.setLength(length[speak]);
    }
}


