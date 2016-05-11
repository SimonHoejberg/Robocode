package MathBotpk;

import robocode.*;
import java.awt.Color;

/**
 * MathBot - a robot created with BTR
 */
public class MathBot extends robocode.Robot {

    private Double turnAmount;
    private Double turnDirection;
    private Boolean moving;
    private Double remainingDistance;

    /**
    * run: TheMachine's default behavior
    */
    public void run() {
        turnAmount = 0.0;
        turnDirection = 1.0;
        moving = false;
        remainingDistance = 0.0;
        setBodyColor(Color.blue);
        setGunColor(Color.green);
        setRadarColor(Color.red);
        setBulletColor(Color.white);
        // Robot main loop
        while(true) {
            if (moving){
                if (remainingDistance < 0.0){
                    back(remainingDistance);
                    moving = new Boolean(false);
                }
                else {
                    remainingDistance -= new Double(20.0);
                    ahead(20.0);
                    scan();
                }
            }
            else {
                turnAmount = new Double((Math.random() * (180.0 - 20.0) + 20.0));
                turnGunRight(turnAmount);
                ahead(Math.pow((Math.random() * 10.0), 2.0));
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
            out.print("I'm the best at maths");
            out.println(". You heard me!");
            moving = new Boolean(true);
            if (e.getHeading() - getHeading() >= 0.0){
                turnDirection = new Double(1.0);
            }
            else {
                turnDirection = new Double(-1.0);
            }
            Double difference = getGunHeading() - getHeading();
            if (difference > 180.0){
                turnGunRight(360.0 - difference);
            }
            else {
                turnGunLeft(difference);
            }
            turnRight(e.getBearing());
            remainingDistance = new Double(Math.ceil(Math.sqrt(e.getDistance() + 5.0)));
            if (remainingDistance > 200.0){
                remainingDistance = new Double(200.0);
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
            turnDirection = new Double(1.0);
        }
        else {
            turnDirection = new Double(-1.0);
        }
        turnRight(e.getBearing());
        fire(Math.floor((Math.random() * 4.0)));
        back((Math.random() * (40.0 - 10.0) + 10.0));
    }

}