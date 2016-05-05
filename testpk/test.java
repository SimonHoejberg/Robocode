package testpk;

import robocode.*;
import java.awt.*;
import java.util.*;

/**
 * test - a robot created with BTR
 */
public class test extends robocode.Robot {

    private Double turnDirection;
    private Boolean moving;
    private Double remainingDistance;
    private ArrayList<Double> victor;
    private ArrayList<Double> jonathan;

    /**
    * run: TheMachine's default behavior
    */
    public void run() {
        turnDirection = 1.0;
        moving = false;
        remainingDistance = 0.0;
        victor = new ArrayList<Double>();
        for (int _i = 0; _i < 10.0; ++_i)
            victor.add(0.0);
        jonathan = new ArrayList<Double>();
        for (int _i = 0; _i < 12.0; ++_i)
            jonathan.add(0.0);
        jonathan.set(5, jonathan.get(5) + 7.0);
        victor = jonathan;
        // Robot main loop
        while(true) {
            if (moving){
                if (remainingDistance > 0.0){
                    ahead(remainingDistance);
                    moving = false;
                }
                else {
                    remainingDistance -= 20.0;
                    ahead(20.0);
                    scan();
                }
            }
            else {
                turnGunRight(1440.0 * turnDirection);
            }
        }
    }

}