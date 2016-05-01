package TheMachine2pk;

import robocode.*;
import java.awt.*;
import java.util.*;

/**
 * TheMachine2 - a robot created with BTR
 */
public class TheMachine2 extends robocode.Robot {

    private Double turnDirection;
    private Boolean moving;
    private Double remainingDistance;

    /**
    * run: TheMachine's default behavior
    */
    public void run() {
        turnDirection = 1.0;
        moving = false;
        remainingDistance = 0.0;
        setBodyColor(Color.gray);
        setGunColor(Color.lightGray);
        setRadarColor(Color.red);
        setBulletColor(Color.black);
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

    @Override
    public void onScannedRobot(ScannedRobotEvent e){
        _onScannedRobot(e);
    }

    private void _onScannedRobot(ScannedRobotEvent e){
        if (moving){
            CleverFire(e.getDistance());
        }
        else {
            moving = true;
            if (e.getHeading() - getHeading() >= 0.0){
                turnDirection = 1.0;
            }
            else {
                turnDirection = -1.0;
            }
            Double difference = getGunHeading() - getHeading();
            if (difference > 180.0){
                turnGunRight(360.0 - difference);
            }
            else {
                turnGunLeft(difference);
            }
            turnRight(e.getBearing());
            remainingDistance = e.getDistance() + 5.0;
            if (remainingDistance > 200.0){
                remainingDistance = 200.0;
            }
            scan();
        }
    }

    public void CleverFire(Double distance){
        if (distance < 50.0){
            fire(3.0);
        }
        else if (distance < 100.0){
            fire(2.0);
        }
        else if (distance < 200.0){
            fire(1.0);
        }
    }

    @Override
    public void onHitRobot(HitRobotEvent e){
        _onHitRobot(e);
    }

    private void _onHitRobot(HitRobotEvent e){
        if (e.getBearing() >= 0.0){
            turnDirection = 1.0;
        }
        else {
            turnDirection = -1.0;
        }
        turnRight(e.getBearing());
        fire(3.0);
        if (e.getEnergy() > 16.0){
            fire(3.0);
        }
        else if (e.getEnergy() > 10.0){
            fire(2.0);
        }
        else if (e.getEnergy() > 4.0){
            fire(1.0);
        }
        else if (e.getEnergy() > 2.0){
            fire(0.5);
        }
        else if (e.getEnergy() > 0.4){
            fire(0.1);
        }
        ahead(40.0);
    }

}